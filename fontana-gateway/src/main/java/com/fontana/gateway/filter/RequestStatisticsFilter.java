package com.fontana.gateway.filter;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.fontana.util.request.IpUtil;
import com.fontana.util.tools.PointUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * 请求统计分析埋点过滤器
 *
 * @author cqf
 * @date 2022/10/7
 */
@Component
public class RequestStatisticsFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        Map<String, String> headers = request.getHeaders().toSingleValueMap();
        UserAgent userAgent = UserAgentUtil.parse(headers.get("User-Agent"));
        //埋点
        PointUtil.debug("1", "request-statistics",
                "ip=" + IpUtil.getRemoteIpAddress(request)
                        + "&browser=" + userAgent.getBrowser().getName()
                        + "&operatingSystem=" + userAgent.getOs().getName());
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return  HIGHEST_PRECEDENCE + 9901;
    }


}
