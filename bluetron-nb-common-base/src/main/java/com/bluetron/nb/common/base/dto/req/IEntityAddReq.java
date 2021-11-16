package com.bluetron.nb.common.base.dto.req;

import java.util.Map;

/**
 * 实体对象新增请求
 *
 * @author genx
 * @date 2021/4/14 9:08
 */
public interface IEntityAddReq<E> {

    /**
     * 扩展数据
     *
     * @return
     */
    Map<String, Object> getExtension();
}
