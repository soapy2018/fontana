package com.fontana.cloud.feign.client;

import com.fontana.base.result.Result;
import com.fontana.base.result.ResultCode;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;


import java.util.List;
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

}
