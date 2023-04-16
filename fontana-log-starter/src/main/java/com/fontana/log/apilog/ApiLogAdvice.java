package com.fontana.log.apilog;

import com.fontana.util.request.WebContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @author chenqingfeng
 * @description api日志切面处理类
 * @date 2022/11/14 9:24
 */
@Slf4j
public class ApiLogAdvice implements MethodInterceptor {

    private ApiLogProperties apiLogProperties;

    public ApiLogAdvice(ApiLogProperties apiLogProperties) {
        this.apiLogProperties = apiLogProperties;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        long startTime = System.currentTimeMillis();
        StringBuilder logMessage = new StringBuilder();
        // 开始打印请求日志
        HttpServletRequest request = null;
        if(WebContextUtil.hasRequestContext()){
            request = WebContextUtil.getHttpRequest();
        }else{
            logMessage.append("**");
        }
        // 打印请求 url
        if(null != request) {
            logMessage.append(request.getRequestURL().toString());
        }
        // 打印调用 controller 的全路径以及执行方法
        logMessage.append("|").append(methodInvocation.getMethod().getDeclaringClass().getName()).append(".").append(methodInvocation.getMethod().getName());
        // 打印请求入参
        if (Boolean.TRUE.equals(apiLogProperties.getShowArgs())) {
            logMessage.append("|").append(Arrays.toString(methodInvocation.getArguments()));
        }else{
            logMessage.append("|**");
        }
        if(null != request) {
            // 打印请求的 IP
            if (Boolean.TRUE.equals(apiLogProperties.getShowIP())) {
                logMessage.append("|").append(request.getRemoteAddr()).append(":").append(request.getRemotePort());
            }else{
                logMessage.append("|**");
            }
            // 打印请求头
            if (Boolean.TRUE.equals(apiLogProperties.getShowHead())) {
                logMessage.append("|").append(WebContextUtil.getHeaderString(request));
            }else{
                logMessage.append("|**");
            }
        }else{
            logMessage.append("|**");
        }
        Object result = methodInvocation.proceed();
        // 打印请求耗时
        logMessage.append("|").append(System.currentTimeMillis() - startTime).append("ms");
        log.info(logMessage.toString());
        return result;
    }

}