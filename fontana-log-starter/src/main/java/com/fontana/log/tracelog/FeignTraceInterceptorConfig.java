package com.fontana.log.tracelog;

import com.fontana.base.constant.HttpConstants;
import com.fontana.util.tools.MDCTraceUtil;
import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * feign拦截器，传递traceId
 *
 * @author cqf
 * @date 2021/1/28
 */
@Configuration
@ConditionalOnClass(value = {RequestInterceptor.class})
public class FeignTraceInterceptorConfig {

    @Resource
    private TraceProperties traceProperties;

    @Bean
    public RequestInterceptor feignTraceInterceptor() {
        return template -> {
            if (traceProperties.getEnabled()) {
                //传递日志traceId
                String traceId = MDCTraceUtil.getTraceId();
                if (!StringUtils.isEmpty(traceId)) {
                    template.header(HttpConstants.TRACE_ID_HEADER, traceId);
                }
            }
        };
    }
}
