package com.fontana.gateway.filter;

import com.fontana.base.constant.HttpConstants;
import com.fontana.log.traceLog.TraceProperties;
import com.fontana.gateway.constant.GatewayConstant;
import com.fontana.util.tools.MDCTraceUtil;
import lombok.extern.slf4j.Slf4j;
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
            final String traceId = MDCTraceUtil.createTraceId();
            MDCTraceUtil.putTrace(traceId);
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
            //gateway也有pre和post两种方式的filter,分别处理前置逻辑和后置逻辑。客户端的请求先经过pre类型的filter，
            // 然后将请求转发到具体的业务服务，收到业务服务的响应之后，再经过post类型的 filter 处理，最后返回响应到客户端。
            //.then就是post后置逻辑，是收到响应后处理，且order越小优先级越高，反而是越晚处理
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
