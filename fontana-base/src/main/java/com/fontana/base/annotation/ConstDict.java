package com.fontana.base.annotation;

import java.lang.annotation.*;

/**
 * 标记java类字典
 * @Author cqf
 * @Date 2022/7/8 10:34
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ConstDict {

    /**
     * 作为字典数据的唯一编码
     */
    String value() default "";
}
