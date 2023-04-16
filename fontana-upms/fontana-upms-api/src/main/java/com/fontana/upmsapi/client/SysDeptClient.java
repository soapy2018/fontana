package com.fontana.upmsapi.client;

import com.fontana.base.result.Result;
import com.fontana.cloud.feign.client.BaseFallbackFactory;
import com.fontana.db.client.BaseClient;
import com.fontana.upmsapi.dto.SysDeptDto;
import com.fontana.upmsapi.vo.SysDeptVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

/**
 * 部门管理服务远程数据操作访问接口。
 *
 * @author cqf
 * @date 2020-08-08
 */
@FeignClient(
        name = "common-upms", contextId="dept",
        fallbackFactory = SysDeptClient.SysDeptClientFallbackFactory.class)
public interface SysDeptClient extends BaseClient<SysDeptDto, SysDeptVo, Long> {

    /**
     * 基于主键的(In-list)条件获取远程数据接口。
     *
     * @param deptIds 主键Id集合。
     * @param withDict 是否包含字典关联。
     * @return 应答结果对象，包含主对象的数据集合。
     */
    @Override
    @PostMapping("/sysDept/listByIds")
    Result<List<SysDeptVo>> listByIds(
            @RequestParam("deptIds") Set<Long> deptIds,
            @RequestParam("withDict") Boolean withDict);

    /**
     * 基于主键Id，获取远程对象。
     *
     * @param deptId 主键Id。
     * @param withDict 是否包含字典关联。
     * @return 应答结果对象，包含主对象数据。
     */
    @Override
    @PostMapping("/sysDept/getById")
    Result<SysDeptVo> getById(
            @RequestParam("deptId") Long deptId,
            @RequestParam("withDict") Boolean withDict);

    /**
     * 判断参数列表中指定的主键Id，是否都存在。
     *
     * @param deptIds 主键Id集合。
     * @return 应答结果对象，包含true全部存在，否则false。
     */
    @Override
    @PostMapping("/sysDept/existIds")
    Result<Boolean> existIds(@RequestParam("deptIds") Set<Long> deptIds);

    /**
     * 判断主键Id是否存在。
     *
     * @param deptId 参数主键Id。
     * @return 应答结果对象，包含true表示存在，否则false。
     */
    @Override
    @PostMapping("/sysDept/existId")
    Result<Boolean> existId(@RequestParam("deptId") Long deptId);

    /**
     * 删除主键Id关联的对象。
     *
     * @param deptId 主键Id。
     * @return 应答结果对象。
     */
    @Override
    @PostMapping("/sysDept/deleteById")
    Result<Integer> deleteById(@RequestParam("deptId") Long deptId);

    /**
     * 删除符合过滤条件的数据。
     *
     * @param filter 过滤对象。
     * @return 应答结果对象，包含删除数量。
     */
    @Override
    @PostMapping("/sysDept/deleteBy")
    Result<Integer> deleteBy(@RequestBody SysDeptDto filter);


    @Component("UpmsSysDeptClientFallbackFactory")
    @Slf4j
    class SysDeptClientFallbackFactory
            extends BaseFallbackFactory<SysDeptDto, SysDeptVo, Long, SysDeptClient> implements SysDeptClient {

        @Override
        public SysDeptClient create(Throwable throwable) {
            log.error("Exception For Feign Remote Call.", throwable);
            return new SysDeptClientFallbackFactory();
        }

    }
}
