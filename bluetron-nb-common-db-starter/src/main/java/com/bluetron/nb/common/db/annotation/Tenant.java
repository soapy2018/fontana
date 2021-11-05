package com.bluetron.nb.common.db.annotation;

import com.bluetron.nb.common.base.context.TenantContextHolder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author chenqingfeng
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Tenant {
    String value() default TenantContextHolder.DEFAULT_TENANT;
}
