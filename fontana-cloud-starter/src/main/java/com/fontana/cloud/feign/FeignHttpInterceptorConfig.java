package com.fontana.cloud.feign;

import com.fontana.base.constant.HttpConstants;
import com.fontana.base.object.TokenData;
import com.fontana.util.request.WebContextUtil;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * feign拦截器，只包含http相关数据
 *
 * @author cqf
 * @date 2021/9/22
 * <p>
 */
public class FeignHttpInterceptorConfig {
    protected List<String> requestHeaders = new ArrayList<>();

    @PostConstruct
    public void initialize() {
        requestHeaders.add(HttpConstants.USER_ID_HEADER);
        requestHeaders.add(HttpConstants.X_USER_ID_HEADER);
        requestHeaders.add(HttpConstants.USER_NAME_HEADER);
        requestHeaders.add(HttpConstants.X_USER_NAME_HEADER);
        requestHeaders.add(HttpConstants.ROLE_HEADER);
        requestHeaders.add(HttpConstants.TENANT_ID_HEADER);
        requestHeaders.add(HttpConstants.X_TENANT_ID_HEADER);
        requestHeaders.add(HttpConstants.FACTORY_ID_HEADER);
        requestHeaders.add(HttpConstants.X_FACTORY_ID_HEADER);
        requestHeaders.add(HttpConstants.AUTHORIZATION_HEADER);
        requestHeaders.add(HttpConstants.CONTENT_TYPE_HEADER);
        //tokenData传递
        requestHeaders.add(TokenData.REQUEST_ATTRIBUTE_NAME);
        //tradeId放在 com.fontana.log.tracelog.FeignTraceInterceptorConfig装载
        //requestHeaders.add(HttpConstants.TRACE_ID_HEADER);
    }

    /**
     * 使用feign client访问别的微服务时，将上游传过来的access_token、username、roles等信息放入header传递给下一个服务
     */
    @Bean
    public RequestInterceptor httpFeignInterceptor() {
        return template -> {
            // 对于非servlet请求发起的远程调用，由于无法获取到标识用户身份的TokenData，因此需要略过下面的HEADER注入。
            // 如：由消息队列consumer发起的远程调用请求。
            if (!WebContextUtil.hasRequestContext()) {
                return;
            }
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                Enumeration<String> headerNames = request.getHeaderNames();
                if (headerNames != null) {
                    String headerName;
                    String headerValue;
                    while (headerNames.hasMoreElements()) {
                        headerName = headerNames.nextElement();
                        if(requestHeaders.parallelStream().anyMatch(headerName::equalsIgnoreCase)){
                            headerValue = request.getHeader(headerName);
                            template.header(headerName, headerValue);
                        }
                    }
                }
            }
        };
    }
}
