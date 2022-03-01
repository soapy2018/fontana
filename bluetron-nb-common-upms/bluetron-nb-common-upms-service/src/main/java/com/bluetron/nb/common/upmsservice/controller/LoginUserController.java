package com.bluetron.nb.common.upmsservice.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.bluetron.nb.common.base.annotation.MyRequestBody;
import com.bluetron.nb.common.base.object.TokenData;
import com.bluetron.nb.common.base.result.Pagination;
import com.bluetron.nb.common.base.result.Result;
import com.bluetron.nb.common.db.object.MyPageParam;
import com.bluetron.nb.common.upmsapi.vo.LoginUserInfoVo;
import com.bluetron.nb.common.util.tools.RedisKeyUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.LinkedList;
import java.util.List;

/**
 * 在线用户控制器对象。
 *
 * @author cqf
 * @date 2020-08-08
 */
@Api(tags = "在线用户接口")
@Slf4j
@RestController
@RequestMapping("/loginUser")
public class LoginUserController {

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 显示在线用户列表。
     *
     * @param loginName 登录名过滤。
     * @param pageParam 分页参数。
     * @return 登录用户信息列表。
     */
    @PostMapping("/list")
    public Result<Pagination<LoginUserInfoVo>> list(
            @MyRequestBody String loginName, @MyRequestBody MyPageParam pageParam) {
        int queryCount = pageParam.getPageNum() * pageParam.getPageSize();
        int skipCount = (pageParam.getPageNum() - 1) * pageParam.getPageSize();
        String patternKey;
        if (StrUtil.isBlank(loginName)) {
            patternKey = RedisKeyUtil.getSessionIdPrefix() + "*";
        } else {
            patternKey = RedisKeyUtil.getSessionIdPrefix(loginName) + "*";
        }
        List<LoginUserInfoVo> LoginUserInfoVoList = new LinkedList<>();
        Iterable<String> keys = redissonClient.getKeys().getKeysByPattern(patternKey);
        for (String key : keys) {
            LoginUserInfoVoList.add(this.buildTokenDataByRedisKey(key));
        }
        LoginUserInfoVoList.sort((o1, o2) -> (int) (o2.getLoginTime().getTime() - o1.getLoginTime().getTime()));
        int toIndex = Math.min(skipCount + pageParam.getPageSize(), LoginUserInfoVoList.size());
        List<LoginUserInfoVo> resultList = LoginUserInfoVoList.subList(skipCount, toIndex);
        return Result.succeed(new Pagination<>(resultList, (long) LoginUserInfoVoList.size()));

    }

    /**
     * 强制下线指定登录会话。
     *
     * @param sessionId 待强制下线的SessionId。
     * @return 应答结果对象。
     */
    @PostMapping("/delete")
    public Result<Void> delete(@MyRequestBody String sessionId) {
        // 为了保证被剔除用户正在进行的操作不被干扰，这里只是删除sessionIdKey即可，这样可以使强制下线操作更加平滑。
        // 比如，如果删除操作权限或数据权限的redis session key，那么正在请求数据的操作就会报错。
        redissonClient.getBucket(RedisKeyUtil.makeSessionIdKey(sessionId)).delete();
        return Result.succeed();
    }

    private LoginUserInfoVo buildTokenDataByRedisKey(String key) {
        RBucket<String> sessionData = redissonClient.getBucket(key);
        TokenData tokenData = JSON.parseObject(sessionData.get(), TokenData.class);
        return BeanUtil.copyProperties(tokenData, LoginUserInfoVo.class);
    }
}
