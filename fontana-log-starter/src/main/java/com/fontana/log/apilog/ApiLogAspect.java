package com.fontana.log.apilog;

import com.fontana.base.constant.CommonConstants;
import com.fontana.util.request.WebContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 日志拦截
 *
 * @author chenqingfeng
 */
@Slf4j
@Aspect
@ConditionalOnProperty(prefix = CommonConstants.APILOG_PREFIX, name = "enabled", havingValue = "true")
@Component
public class ApiLogAspect {

    private ApiLogProperties apiLogProperties;

    public ApiLogAspect(ApiLogProperties apiLogProperties) {
        this.apiLogProperties = apiLogProperties;
    }

    @Pointcut("execution(public * com.bluetron..*.interfaces..*.*(..)) ||"
            + "execution(public * com.bluetron..*.controller..*.*(..)) ||"
            + "execution(public * cn.bluetron..*.interfaces..*.*(..)) ||"
            + "execution(public * cn.bluetron..*.controller..*.*(..)) ||"
            + "execution(public * com.fontana..*.controller..*.*(..))"
    )
    public void webLog() {
    }


    /**
     * 环绕
     *
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

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
        logMessage.append("|").append(proceedingJoinPoint.getSignature().getDeclaringTypeName()).append(".").append(proceedingJoinPoint.getSignature().getName());
        // 打印请求入参
        if (Boolean.TRUE.equals(apiLogProperties.getShowArgs())) {
            logMessage.append("|").append(Arrays.toString(proceedingJoinPoint.getArgs()));
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
        Object result = proceedingJoinPoint.proceed();
        // 打印请求耗时
        logMessage.append("|").append(System.currentTimeMillis() - startTime).append("ms");
        log.info(logMessage.toString());
        return result;
    }

}
