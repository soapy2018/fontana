package com.bluetron.nb.common.cloud.feign.client;

import com.bluetron.nb.common.base.result.Pagination;
import com.bluetron.nb.common.base.result.Result;
import com.bluetron.nb.common.base.result.ResultCode;
import com.bluetron.nb.common.db.object.MyAggregationParam;
import com.bluetron.nb.common.db.object.MyQueryParam;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * FeignClient 熔断降级处理对象。
 *
 * @param <D> 主DomainDto域数据对象类型。
 * @param <V> 主DomainVo域数据对象类型。
 * @param <K> 主键类型。
 * @param <T> Feign客户端对象类型。
 * @author cqf
 * @date 2020-08-08
 */
@Slf4j
public abstract class BaseFallbackFactory<D, V, K, T extends BaseClient<D, V, K>>
        implements FallbackFactory<T>, BaseClient<D, V, K> {

    @Override
    public Result<List<V>> listByIds(Set<K> idSet, Boolean withDict) {
        return Result.failed(ResultCode.RPC_DATA_ACCESS_FAILED);
    }

    @Override
    public Result<V> getById(K id, Boolean withDict) {
        return Result.failed(ResultCode.RPC_DATA_ACCESS_FAILED);
    }

    @Override
    public Result<Boolean> existIds(Set<K> idSet) {
        return Result.failed(ResultCode.RPC_DATA_ACCESS_FAILED);
    }

    @Override
    public Result<Boolean> existId(K id) {
        return Result.failed(ResultCode.RPC_DATA_ACCESS_FAILED);
    }

    @Override
    public Result<Integer> deleteById(K id) {
        return Result.failed(ResultCode.RPC_DATA_ACCESS_FAILED);
    }

    @Override
    public Result<Integer> deleteBy(D filter) {
        return Result.failed(ResultCode.RPC_DATA_ACCESS_FAILED);
    }

    @Override
    public Result<Pagination<V>> listBy(MyQueryParam queryParam) {
        return Result.failed(ResultCode.RPC_DATA_ACCESS_FAILED);
    }

    @Override
    public Result<V> getBy(MyQueryParam queryParam) {
        return Result.failed(ResultCode.RPC_DATA_ACCESS_FAILED);
    }

    @Override
    public Result<Pagination<Map<String, Object>>> listMapBy(MyQueryParam queryParam) {
        return Result.failed(ResultCode.RPC_DATA_ACCESS_FAILED);
    }

    @Override
    public Result<Integer> countBy(MyQueryParam queryParam) {
        return Result.failed(ResultCode.RPC_DATA_ACCESS_FAILED);
    }

    @Override
    public Result<List<Map<String, Object>>> aggregateBy(MyAggregationParam aggregationParam) {
        return Result.failed(ResultCode.RPC_DATA_ACCESS_FAILED);
    }

    @Override
    public Result<Pagination<V>> listByNotInList(MyQueryParam queryParam) {
        return Result.failed(ResultCode.RPC_DATA_ACCESS_FAILED);
    }
}
