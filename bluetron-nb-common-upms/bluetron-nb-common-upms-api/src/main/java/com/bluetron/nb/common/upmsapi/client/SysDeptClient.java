package com.bluetron.nb.common.upmsapi.client;

import com.bluetron.nb.common.base.result.Pagination;
import com.bluetron.nb.common.base.result.Result;
import com.bluetron.nb.common.cloud.feign.client.BaseClient;
import com.bluetron.nb.common.cloud.feign.client.BaseFallbackFactory;
import com.bluetron.nb.common.db.object.MyAggregationParam;
import com.bluetron.nb.common.db.object.MyQueryParam;
import com.bluetron.nb.common.upmsapi.dto.SysDeptDto;
import com.bluetron.nb.common.upmsapi.vo.SysDeptVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 部门管理服务远程数据操作访问接口。
 *
 * @author cqf
 * @date 2020-08-08
 */
@FeignClient(
        name = "common-upms",
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

    /**
     * 获取远程主对象中符合查询条件的数据列表。
     *
     * @param queryParam 查询参数。
     * @return 应答结果对象，包含实体对象集合。
     */
    @Override
    @PostMapping("/sysDept/listBy")
    Result<Pagination<SysDeptVo>> listBy(@RequestBody MyQueryParam queryParam);

    /**
     * 获取远程主对象中符合查询条件的单条数据对象。
     *
     * @param queryParam 查询参数。
     * @return 应答结果对象，包含实体对象。
     */
    @Override
    @PostMapping("/sysDept/getBy")
    Result<SysDeptVo> getBy(@RequestBody MyQueryParam queryParam);

    /**
     * 获取远程主对象中符合查询条件的数据列表。
     * 和listBy接口相比，以Map列表的方式返回的主要目的是，降低服务之间的耦合度。
     *
     * @param queryParam 查询参数。
     * @return 应答结果对象，包含主对象集合。
     */
    @Override
    @PostMapping("/sysDept/listMapBy")
    Result<Pagination<Map<String, Object>>> listMapBy(@RequestBody MyQueryParam queryParam);

    /**
     * 获取远程主对象中符合查询条件的数据数量。
     *
     * @param queryParam 查询参数。
     * @return 应答结果对象，包含结果数量。
     */
    @Override
    @PostMapping("/sysDept/countBy")
    Result<Integer> countBy(@RequestBody MyQueryParam queryParam);

    /**
     * 获取远程对象中符合查询条件的分组聚合计算Map列表。
     *
     * @param aggregationParam 聚合参数。
     * @return 应该结果对象，包含聚合计算后的分组Map列表。
     */
    @Override
    @PostMapping("/sysDept/aggregateBy")
    Result<List<Map<String, Object>>> aggregateBy(@RequestBody MyAggregationParam aggregationParam);

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
