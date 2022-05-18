package com.fontana.cloud.feign.client;

import com.fontana.base.result.Result;

import java.util.List;
import java.util.Set;

/**
 * 远程调用接口。
 *
 * @param <D> 主DomainDto域数据对象类型。
 * @param <V> 主DomainVo域数据对象类型。
 * @param <K> 主键类型。
 * @author cqf
 * @date 2020-08-08
 */
public interface BaseClient<D, V, K> {

    /**
     * 基于主键的(in list)获取远程数据接口。
     *
     * @param filterIds 主键Id集合。
     * @param withDict  是否包含字典关联。
     * @return 应答结果对象，包含主对象集合。
     */
    Result<List<V>> listByIds(Set<K> filterIds, Boolean withDict);

    /**
     * 基于主键Id，获取远程对象。
     *
     * @param id       主键Id。
     * @param withDict 是否包含字典关联。
     * @return 应答结果对象，包含主对象数据。
     */
    Result<V> getById(K id, Boolean withDict);

    /**
     * 判断参数列表中指定的主键Id，是否全部存在。
     *
     * @param filterIds 主键Id集合。
     * @return 应答结果对象，包含true全部存在，否则false。
     */
    Result<Boolean> existIds(Set<K> filterIds);

    /**
     * 给定主键Id是否存在。
     *
     * @param id 主键Id。
     * @return 应答结果对象，包含true表示存在，否则false。
     */
    Result<Boolean> existId(K id);

    /**
     * 删除主键Id关联的对象。
     *
     * @param id 主键Id。
     * @return 应答结果对象。
     */
     default Result<Integer> deleteById(K id) {
         throw new UnsupportedOperationException();
     }

    /**
     * 删除符合过滤条件的数据。
     *
     * @param filter 过滤对象。
     * @return 应答结果对象，包含删除数量。
     */
     default Result<Integer> deleteBy(D filter) {
         throw new UnsupportedOperationException();
     }

}
