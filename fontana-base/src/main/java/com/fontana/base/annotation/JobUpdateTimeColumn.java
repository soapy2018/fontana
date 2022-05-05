package com.fontana.base.annotation;

import java.lang.annotation.*;

/**
 * 主要用于标记更新字段。
 *
 * @author cqf
 * @date 2021-06-06
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JobUpdateTimeColumn {

}
