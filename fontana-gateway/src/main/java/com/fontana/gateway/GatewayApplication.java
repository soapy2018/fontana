package com.fontana.gateway;

import com.fontana.gateway.filter.AuthenticationPostFilter;
import com.fontana.gateway.filter.AuthenticationPreFilter;
import com.fontana.gateway.filter.RequestLogFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 网关服务启动类。
 *
 * @author cqf
 * @date 2020-08-08
 */
@SpringCloudApplication
public class GatewayApplication {

    @RestController
    @RequestMapping("/fallback")
    static class FallbackController {
        @GetMapping("")
        public String fallback() {
            return "GATEWAY FALLBACK!!!";
        }
    }

    @Bean
    public AuthenticationPreFilter authenticationPreFilter() {
        return new AuthenticationPreFilter();
    }

    @Bean
    public AuthenticationPostFilter authenticationPostFilter() {
        return new AuthenticationPostFilter();
    }

    @Bean
    public RequestLogFilter requestLogPreFilter() {
        return new RequestLogFilter();
    }

//    @Bean
//    ApplicationContextHolder applicationContextHolder() {
//        return new ApplicationContextHolder();
//    }

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}
