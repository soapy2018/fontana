package com.bluetron.nb.common.gateway.filter;

import com.bluetron.nb.common.base.constant.HttpConstants;
import com.bluetron.nb.common.log.traceLog.TraceProperties;
import com.bluetron.nb.common.util.tools.IdUtil;
import com.bluetron.nb.common.gateway.constant.GatewayConstant;
import com.bluetron.nb.common.util.tools.MDCTraceUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 链路日志前置过虑器。
 * 为整个链路生成唯一的traceId，并存储在Request Head中。
 *
 * @author cqf
 * @date 2020-08-08
 */
@Slf4j
public class RequestLogFilter implements GlobalFilter, Ordered {

    @Autowired
    private TraceProperties traceProperties;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (traceProperties.getEnable()) {
            final String traceId = MDCTraceUtils.createTraceId();
            MDCTraceUtils.putTrace(traceId);
            log.info("开始请求，app={common-gateway}, url={}", exchange.getRequest().getURI().getPath());
            // 分别记录traceId和执行开始时间。
            exchange.getAttributes().put(GatewayConstant.START_TIME_ATTRIBUTE, System.currentTimeMillis());
            ServerHttpRequest mutableReq = exchange.getRequest().mutate().header(
                    HttpConstants.TRACE_ID_HEADER, traceId).build();
            ServerWebExchange mutableExchange = exchange.mutate().request(mutableReq).build();
            ServerHttpResponse response = mutableExchange.getResponse();
            response.beforeCommit(() -> {
                response.getHeaders().set(HttpConstants.TRACE_ID_HEADER, traceId);
                return Mono.empty();
            });
            return chain.filter(mutableExchange).then(Mono.fromRunnable(() -> {
                Long startTime = exchange.getAttribute(GatewayConstant.START_TIME_ATTRIBUTE);
                long elapse = 0;
                if (startTime != null) {
                    elapse = System.currentTimeMillis() - startTime;
                }
                log.info("请求完成, app={common-gateway}, url={}，elapse={}", exchange.getRequest().getURI().getPath(), elapse);
            }));
        }
        return chain.filter(exchange);
    }

    /**
     * 返回过滤器在在调用链上的优先级。
     *
     * @return 数值越低，优先级越高。
     */
    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 9900;
    }
}
