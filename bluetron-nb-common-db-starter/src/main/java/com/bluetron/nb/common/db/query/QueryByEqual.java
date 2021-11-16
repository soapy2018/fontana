package com.bluetron.nb.common.db.query;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标识该字段强制用 = 条件查询
 *
 * @author genx
 * @date 2021/10/8 14:59
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryByEqual {
}
