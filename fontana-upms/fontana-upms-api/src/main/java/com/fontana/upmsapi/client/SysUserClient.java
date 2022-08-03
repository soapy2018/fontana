package com.fontana.upmsapi.client;

import com.fontana.base.result.Result;
import com.fontana.base.result.ResultCode;
import com.fontana.cloud.feign.client.BaseClient;
import com.fontana.cloud.feign.client.BaseFallbackFactory;
import com.fontana.upmsapi.dto.SysUserDto;
import com.fontana.upmsapi.vo.SysUserVo;
import feign.Headers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

/**
 * 用户管理服务远程数据操作访问接口。
 *
 * @author cqf
 * @date 2020-08-08
 */
@FeignClient(
        name = "common-upms", contextId="user",
        fallbackFactory = SysUserClient.SysUserClientFallbackFactory.class)
public interface SysUserClient extends BaseClient<SysUserDto, SysUserVo, Long> {

    /**
     * 基于主键的(In-list)条件获取远程数据接口。
     *
     * @param userIds 主键Id集合。
     * @param withDict 是否包含字典关联。
     * @return 应答结果对象，包含主对象的数据集合。
     */
    @Override
    @PostMapping("/sysUser/listByIds")
    Result<List<SysUserVo>> listByIds(
            @RequestParam("userIds") Set<Long> userIds,
            @RequestParam("withDict") Boolean withDict);

    @GetMapping("/sysUser/listIds")
    Result<List<SysUserVo>> listIds(
            @RequestParam("userIds") Set<Long> userIds,
            @RequestParam("withDict") Boolean withDict);


    @PostMapping("/sysUser/listUsers")
    Result<List<SysUserVo>> listUsers(
            @RequestBody Set<Long> userIds);

    /**
     * 基于主键Id，获取远程对象。
     *
     * @param userId 主键Id。
     * @param withDict 是否包含字典关联。
     * @return 应答结果对象，包含主对象数据。
     */
    @Override
    @PostMapping("/sysUser/getById")
    Result<SysUserVo> getById(
            @RequestParam("userId") Long userId,
            @RequestParam("withDict") Boolean withDict);

    /**
     * 判断参数列表中指定的主键Id，是否都存在。
     *
     * @param userIds 主键Id集合。
     * @return 应答结果对象，包含true全部存在，否则false。
     */
    @Override
    @PostMapping("/sysUser/existIds")
    Result<Boolean> existIds(@RequestParam("userIds") Set<Long> userIds);

    /**
     * 判断主键Id是否存在。
     *
     * @param userId 参数主键Id。
     * @return 应答结果对象，包含true表示存在，否则false。
     */
    @Override
    @PostMapping("/sysUser/existId")
    Result<Boolean> existId(@RequestParam("userId") Long userId);

    /**
     * 删除主键Id关联的对象。
     *
     * @param userId 主键Id。
     * @return 应答结果对象。
     */
    @Override
    @PostMapping("/sysUser/deleteById")
    Result<Integer> deleteById(@RequestParam("userId") Long userId);

    /**
     * 删除符合过滤条件的数据。
     *
     * @param filter 过滤对象。
     * @return 应答结果对象，包含删除数量。
     */
    @Override
    @PostMapping("/sysUser/deleteBy")
    Result<Integer> deleteBy(@RequestBody SysUserDto filter);

    @Component("UpmsSysUserClientFallbackFactory")
    @Slf4j
    class SysUserClientFallbackFactory
            extends BaseFallbackFactory<SysUserDto, SysUserVo, Long, SysUserClient> implements SysUserClient {

        @Override
        public SysUserClient create(Throwable throwable) {
            log.error("Exception For Feign Remote Call.", throwable);
            return new SysUserClientFallbackFactory();
        }

        @Override
        public Result<List<SysUserVo>> listIds(Set<Long> userIds, Boolean withDict) {
            return Result.failed(ResultCode.RPC_DATA_ACCESS_FAILED);
        }

        @Override
        public Result<List<SysUserVo>> listUsers(Set<Long> userIds) {
            return Result.failed(ResultCode.RPC_DATA_ACCESS_FAILED);
        }
    }
}
