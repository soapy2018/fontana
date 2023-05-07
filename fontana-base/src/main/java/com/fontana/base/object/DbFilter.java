package com.fontana.base.object;

import com.fontana.base.constant.FieldFilterType;
import lombok.Data;

import java.util.Set;

/**
 * 数据过滤参数对象。
 *
 * @author cqf
 * @date 2023-01-06
 */
@Data
public class DbFilter {

    /**
     * 表名。
     */
    private String tableName;

    /**
     * 过滤字段名。
     */
    private String columnName;

    /**
     * 过滤值。
     */
    private Object columnValue;

    /**
     * 范围比较的最小值。
     */
    private Object columnValueStart;

    /**
     * 范围比较的最大值。
     */
    private Object columnValueEnd;

    /**
     * 仅当操作符为IN的时候使用。
     */
    private Set<Object> columnValueList;

    /**
     * 过滤类型，参考FieldFilterType常量对象。缺省值就是等于过滤了。
     */
    private Integer filterType = FieldFilterType.EQUAL_FILTER;
}
