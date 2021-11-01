package com.bluetron.nb.common.db.query;

import com.bluetron.nb.common.util.tools.GenericTypeKitUtil;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * Description: 
 * @author genx
 * @date 2021/9/30 10:49
 */
public interface IQueryCondition<T> {

    Set<Map.Entry<String, Object>> entrySet();

    String getOrderBy();

    boolean isAsc();

    int getPage();

    int getPageSize();

    /**
     * 获取查询类
     * @return
     */
    default Class<T> getQueryBeanType() {
        return GenericTypeKitUtil.getGenericType(IQueryCondition.class, getClass(), 0);
    }

}
