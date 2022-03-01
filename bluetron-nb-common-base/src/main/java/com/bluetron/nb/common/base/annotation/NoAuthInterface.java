package com.bluetron.nb.common.base.annotation;

import java.lang.annotation.*;

/**
 * 主要用于标记无需Token验证的接口
 *
 * @author cqf
 * @date 2021-06-06
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoAuthInterface {
}
