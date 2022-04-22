package com.bluetron.nb.common.log.traceLog;

import com.bluetron.nb.common.base.constant.HttpConstants;
import com.bluetron.nb.common.util.tools.MDCTraceUtil;
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
public class FeignTraceConfig {

    @Resource
    private TraceProperties traceProperties;

    @Bean
    public RequestInterceptor feignTraceInterceptor() {
        return template -> {
            if (traceProperties.getEnable()) {
                //传递日志traceId
                String traceId = MDCTraceUtil.getTraceId();
                if (!StringUtils.isEmpty(traceId)) {
                    template.header(HttpConstants.TRACE_ID_HEADER, traceId);
                }
            }
        };
    }
}
