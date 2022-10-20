package com.fontana.log.requestlog.config;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author yegenchang
 * @description
 * @date 2022/6/20 14:19
 */
@Deprecated
public class RequestLogRegistrar implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        return new String[]{
                "com.fontana.log.requestlog.advice.RequestLogAdvice",
                "com.fontana.log.requestlog.filter.RequestLogFilter",
        };
    }
}
