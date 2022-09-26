package com.fontana.log.tracelog;

import com.fontana.base.constant.HttpConstants;
import com.fontana.util.request.WebContextUtil;
import com.fontana.util.tools.MDCTraceUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * web过滤器，生成日志链路追踪id，并赋值MDC
 * 此filter把traceId放到MDC里，filter塞到header则中由 {@link FeignTraceInterceptorConfig 完成}
 *  filter在拦截器外层，请求进来是先filter后拦截器
 * @author cqf
 * @date 2021/10/14
 * <p>
 */
@Component
@ConditionalOnClass(value = {HttpServletRequest.class, OncePerRequestFilter.class})
@Order(value = MDCTraceUtil.FILTER_ORDER)
public class WebTraceFilter extends OncePerRequestFilter {
    @Resource
    private TraceProperties traceProperties;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !traceProperties.getEnabled();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        try {
            String traceId = WebContextUtil.getTraceId(request);
            if (StringUtils.isEmpty(traceId)) {
                MDCTraceUtil.addTrace();
            } else {
                MDCTraceUtil.putTrace(traceId);
            }
            filterChain.doFilter(request, response);
        } finally {
            MDCTraceUtil.removeTrace();
        }
    }
}
