package com.fontana.log.requestlog.annotation;

import com.fontana.log.requestlog.config.RequestLogRegister;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author yegenchang
 * @description 是否开启请求日志注解
 * @date 2022/6/20 14:11
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Import({RequestLogRegister.class})
@Deprecated
public @interface EnableRequestLog {

}
