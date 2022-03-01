package com.bluetron.nb.common.upmsservice.controller;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.bluetron.nb.common.base.annotation.MyRequestBody;
import com.bluetron.nb.common.base.annotation.NoAuthInterface;
import com.bluetron.nb.common.base.constant.CommonConstants;
import com.bluetron.nb.common.base.object.TokenData;
import com.bluetron.nb.common.base.result.Result;
import com.bluetron.nb.common.base.result.ResultCode;
import com.bluetron.nb.common.log.auditLog.AuditLog;
import com.bluetron.nb.common.onlineservice.service.OnlineColumnService;
import com.bluetron.nb.common.redis.util.RedisTemplateUtil;
import com.bluetron.nb.common.redis.util.SessionCacheHelper;
import com.bluetron.nb.common.upmsapi.dict.SysUserStatus;
import com.bluetron.nb.common.upmsapi.dict.SysUserType;
import com.bluetron.nb.common.upmsservice.entity.SysMenu;
import com.bluetron.nb.common.upmsservice.entity.SysUser;
import com.bluetron.nb.common.upmsservice.service.*;
import com.bluetron.nb.common.util.codec.Md5Util;
import com.bluetron.nb.common.util.codec.RsaUtil;
import com.bluetron.nb.common.util.lang.StringUtil;
import com.bluetron.nb.common.util.request.IpUtil;
import com.bluetron.nb.common.util.request.WebContextUtil;
import com.bluetron.nb.common.util.tools.IdUtil;
import com.bluetron.nb.common.util.tools.RandImageUtil;
import com.bluetron.nb.common.util.tools.RedisKeyUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;

/**
 * @Author cqf
 * @since 2021-12-17
 */
@RestController
@RequestMapping("/login")
@Api(tags="用户登录")
@Slf4j
public class LoginController {

	/**
	 * 后台生成图形验证码 ：有效
	 * @param response
	 * @param key
	 */
	@Autowired
	RedisTemplateUtil redisTemplateUtil;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysPermCodeService sysPermCodeService;
	@Autowired
	private SysPermService sysPermService;
	@Autowired
	private SysMenuService sysMenuService;
	@Autowired
	private SysDataPermService sysDataPermService;
	@Autowired
	private SysPermWhitelistService sysPermWhitelistService;
	@Autowired
	private RedissonClient redissonClient;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private SessionCacheHelper cacheHelper;

	@ApiOperation("获取验证码")
	@NoAuthInterface
	@GetMapping(value = "/randomImage/{key}")
	public Result<String> randomImage(HttpServletResponse response, @PathVariable String key){
		Result<String> res = new Result<String>();
		try {
			String code = RandomUtil.randomString(CommonConstants.BASE_CHECK_CODES,4);
			String lowerCaseCode = code.toLowerCase();
			String realKey = Md5Util.md5(lowerCaseCode + key);
			redisTemplateUtil.setExpire(realKey, lowerCaseCode, 60);
			String base64 = RandImageUtil.generate(code);
			return Result.succeed(base64);
		} catch (Exception e) {
			log.error("获取验证码出错: {}" , e.getMessage());
			return Result.failed(ResultCode.ACQUIRE_CHECK_CODES_ERROR);

		}
	}

	/**
	 * 登录接口。
	 *
	 * @param loginName 登录名。
	 * @param password  密码。
	 * @return 应答结果对象，其中包括JWT的Token数据，以及菜单列表。
	 */
	@ApiOperation("登录接口")
	@NoAuthInterface
	@PostMapping("/doLogin")
	public Result<JSONObject> doLogin(
			@MyRequestBody String loginName, @MyRequestBody String password/*, @MyRequestBody String captcha*/) throws Exception {

		if(StringUtil.isAnyBlank(loginName, password/*, captcha*/)){
			return Result.failed(ResultCode.PARAM_IS_BLANK);
		}
		SysUser user = sysUserService.getSysUserByLoginName(loginName);
		password = URLDecoder.decode(password, StandardCharsets.UTF_8.name());
		// NOTE: 第一次使用时，请务必阅读ApplicationConstant.PRIVATE_KEY的代码注释。
		// 执行RsaUtil工具类中的main函数，可以生成新的公钥和私钥。
		password = RsaUtil.decrypt(password, CommonConstants.PRIVATE_KEY);
		// 校验密码
		if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
			return Result.failed(ResultCode.USER_LOGIN_ERROR);
		}
		if (user.getUserStatus() == SysUserStatus.STATUS_LOCKED) {
			return Result.failed(ResultCode.USER_ACCOUNT_LOCKED);
		}
		String patternKey = RedisKeyUtil.getSessionIdPrefix(user.getLoginName(), WebContextUtil.getDeviceType()) + "*";;
		redissonClient.getKeys().deleteByPatternAsync(patternKey);
		JSONObject jsonData = this.buildLoginData(user);
		return Result.succeed(jsonData);

	}

	private JSONObject buildLoginData(SysUser user) {
		int deviceType = WebContextUtil.getDeviceType();
		boolean isAdmin = user.getUserType() == SysUserType.TYPE_ADMIN;
		TokenData tokenData = new TokenData();
		String sessionId = user.getLoginName() + "_" + deviceType + "_" + IdUtil.generateUUID();
		tokenData.setUserId(user.getUserId());
		tokenData.setDeptId(user.getDeptId());
		tokenData.setIsAdmin(isAdmin);
		tokenData.setLoginName(user.getLoginName());
		tokenData.setShowName(user.getShowName());
		tokenData.setSessionId(sessionId);
		tokenData.setLoginIp(IpUtil.getRemoteIpAddress(WebContextUtil.getHttpRequest()));
		tokenData.setLoginTime(new Date());
		tokenData.setDeviceType(deviceType);
		// 这里手动将TokenData存入request，便于OperationLogAspect统一处理操作日志。
		WebContextUtil.addTokenToRequest(tokenData);
		JSONObject jsonData = new JSONObject();
		jsonData.put(TokenData.REQUEST_ATTRIBUTE_NAME, tokenData);
		jsonData.put("showName", user.getShowName());
		jsonData.put("isAdmin", isAdmin);
		Collection<SysMenu> menuList;
		Collection<String> permCodeList;
		if (isAdmin) {
			menuList = sysMenuService.getAllMenuList();
			permCodeList = sysPermCodeService.getAllPermCodeList();
		} else {
			menuList = sysMenuService.getMenuListByUserId(tokenData.getUserId());
			permCodeList = sysPermCodeService.getPermCodeListByUserId(user.getUserId());
			// 将白名单url列表合并到当前用户的权限资源列表中，便于网关一并处理。
			Collection<String> permList = sysPermService.getPermListByUserId(user.getUserId());
			permList.addAll(sysPermWhitelistService.getWhitelistPermList());
			jsonData.put("permSet", permList);
		}
		jsonData.put("menuList", menuList);
		jsonData.put("permCodeList", permCodeList);
		if (user.getUserType() != SysUserType.TYPE_ADMIN) {
			sysDataPermService.putDataPermCache(sessionId, user.getUserId(), user.getDeptId());
		}
		return jsonData;
	}

	/**
	 * 登出操作。同时将Session相关的信息从缓存中删除。
	 *
	 * @return 应答结果对象。
	 */
	//@OperationLog(type = SysOperationLogType.LOGOUT)
	@AuditLog(operation="用户登出")
	@PostMapping("/doLogout")
	public Result<Void> doLogout() {
		TokenData tokenData = WebContextUtil.takeTokenFromRequest();
		sysDataPermService.removeDataPermCache(tokenData.getSessionId());
		cacheHelper.removeAllSessionCache(tokenData.getSessionId());
		return Result.succeed();
	}

	/**
	 * 在登录之后，通过token再次获取登录信息。
	 * 用于在当前浏览器登录系统后，在新tab页中可以免密登录。
	 *
	 * @return 应答结果对象，其中包括JWT的Token数据，以及菜单列表。
	 */
	@GetMapping("/getLoginInfo")
	public Result<JSONObject> getLoginInfo() {
		TokenData tokenData = WebContextUtil.takeTokenFromRequest();
		// 这里解释一下为什么没有缓存menuList和permCodeList。
		// 1. 该操作和权限验证不同，属于低频操作。
		// 2. 第一次登录和再次获取登录信息之间，如果修改了用户的权限，那么本次获取的是最新权限。
		// 3. 上一个问题无法避免，因为即便缓存也是有过期时间的，过期之后还是要从数据库获取的。
		JSONObject jsonData = new JSONObject();
		jsonData.put("showName", tokenData.getShowName());
		jsonData.put("isAdmin", tokenData.getIsAdmin());
		Collection<SysMenu> menuList;
		Collection<String> permCodeList;
		if (tokenData.getIsAdmin()) {
			menuList = sysMenuService.getAllMenuList();
			permCodeList = sysPermCodeService.getAllPermCodeList();
		} else {
			menuList = sysMenuService.getMenuListByUserId(tokenData.getUserId());
			permCodeList = sysPermCodeService.getPermCodeListByUserId(tokenData.getUserId());
		}
		jsonData.put("menuList", menuList);
		jsonData.put("permCodeList", permCodeList);
		return Result.succeed(jsonData);
	}

	/**
	 * 用户修改自己的密码。
	 *
	 * @param oldPass 原有密码。
	 * @param newPass 新密码。
	 * @return 应答结果对象。
	 */
	@PostMapping("/changePassword")
	public Result<Void> changePassword(
			@MyRequestBody String oldPass, @MyRequestBody String newPass) throws Exception {
		if (StringUtil.isAnyBlank(newPass, oldPass)) {
			return Result.failed(ResultCode.PARAM_IS_BLANK);
		}
		TokenData tokenData = WebContextUtil.takeTokenFromRequest();
		SysUser user = sysUserService.getById(tokenData.getUserId());
		oldPass = URLDecoder.decode(oldPass, StandardCharsets.UTF_8.name());
		// NOTE: 第一次使用时，请务必阅读ApplicationConstant.PRIVATE_KEY的代码注释。
		// 执行RsaUtil工具类中的main函数，可以生成新的公钥和私钥。
		oldPass = RsaUtil.decrypt(oldPass, CommonConstants.PRIVATE_KEY);
		if (user == null || !passwordEncoder.matches(oldPass, user.getPassword())) {
			return Result.failed(ResultCode.USER_LOGIN_ERROR);
		}
		newPass = URLDecoder.decode(newPass, StandardCharsets.UTF_8.name());
		newPass = RsaUtil.decrypt(newPass, CommonConstants.PRIVATE_KEY);
		if (!sysUserService.changePassword(tokenData.getUserId(), newPass)) {
			return Result.failed(ResultCode.DATA_NOT_EXIST);
		}
		return Result.succeed();
	}
}