package com.bluetron.nb.common.db.util;


import com.bluetron.nb.common.base.vo.SysPermissionDataRuleVo;
import com.bluetron.nb.common.base.vo.SysUserCacheInfo;
import com.bluetron.nb.common.util.request.WebContextUtil;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: JeecgDataAutorUtils
 * @Description: 数据权限查询规则容器工具类
 * @Author: 张代浩
 * @Date: 2012-12-15 下午11:27:39
 * 
 */
public class DataAutorUtils {
	
	public static final String MENU_DATA_AUTHOR_RULES = "MENU_DATA_AUTHOR_RULES";
	
	public static final String MENU_DATA_AUTHOR_RULE_SQL = "MENU_DATA_AUTHOR_RULE_SQL";
	
	public static final String SYS_USER_INFO = "SYS_USER_INFO";

	/**
	 * 往链接请求里面，传入数据查询条件
	 * 
	 * @param request
	 * @param dataRules
	 */
	public static synchronized void installDataSearchConditon(HttpServletRequest request, List<SysPermissionDataRuleVo> dataRules) {
		@SuppressWarnings("unchecked")
		List<SysPermissionDataRuleVo> list = (List<SysPermissionDataRuleVo>)loadDataSearchConditon();// 1.先从request获取MENU_DATA_AUTHOR_RULES，如果存则获取到LIST
		if (list==null) {
			// 2.如果不存在，则new一个list
			list = new ArrayList<SysPermissionDataRuleVo>();
		}
		for (SysPermissionDataRuleVo tsDataRule : dataRules) {
			list.add(tsDataRule);
		}
		request.setAttribute(MENU_DATA_AUTHOR_RULES, list); // 3.往list里面增量存指
	}

	/**
	 * 获取请求对应的数据权限规则
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static synchronized List<SysPermissionDataRuleVo> loadDataSearchConditon() {
		return (List<SysPermissionDataRuleVo>) WebContextUtil.getHttpRequest().getAttribute(MENU_DATA_AUTHOR_RULES);
				
	}

	/**
	 * 获取请求对应的数据权限SQL
	 * 
	 * @return
	 */
	public static synchronized String loadDataSearchConditonSQLString() {
		return (String) WebContextUtil.getHttpRequest().getAttribute(MENU_DATA_AUTHOR_RULE_SQL);
	}

	/**
	 * 往链接请求里面，传入数据查询条件
	 * 
	 * @param request
	 * @param sql
	 */
	public static synchronized void installDataSearchConditon(HttpServletRequest request, String sql) {
		String ruleSql = (String)loadDataSearchConditonSQLString();
		if (!StringUtils.hasText(ruleSql)) {
			request.setAttribute(MENU_DATA_AUTHOR_RULE_SQL,sql);
		}
	}

	/**
	 * 将用户信息存到request
	 * @param request
	 * @param userinfo
	 */
	public static synchronized void installUserInfo(HttpServletRequest request, SysUserCacheInfo userinfo) {
		request.setAttribute(SYS_USER_INFO, userinfo);
	}

	/**
	 * 将用户信息存到request
	 * @param userinfo
	 */
	public static synchronized void installUserInfo(SysUserCacheInfo userinfo) {
		WebContextUtil.getHttpRequest().setAttribute(SYS_USER_INFO, userinfo);
	}

	/**
	 * 从request获取用户信息
	 * @return
	 */
	public static synchronized SysUserCacheInfo loadUserInfo() {
		return (SysUserCacheInfo) WebContextUtil.getHttpRequest().getAttribute(SYS_USER_INFO);
				
	}
}