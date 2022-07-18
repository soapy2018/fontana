package com.fontana.log.apiLog;

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
@ConditionalOnProperty(prefix = CommonConstants.APILOG_PREFIX, name = "enable", havingValue = "true")
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
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 打印请求 url
        logMessage.append(request.getRequestURL().toString());
        //log.info("{} api : {}", request.getMethod(), request.getRequestURL().toString());
        // 打印调用 controller 的全路径以及执行方法
        logMessage.append("|").append(proceedingJoinPoint.getSignature().getDeclaringTypeName()).append(".").append(proceedingJoinPoint.getSignature().getName());
        //log.info("Class Method : {}.{}", proceedingJoinPoint.getSignature().getDeclaringTypeName(), proceedingJoinPoint.getSignature().getName());
        // 打印请求入参
        if (apiLogProperties.getShowArgs()) {
            logMessage.append("|").append(Arrays.toString(proceedingJoinPoint.getArgs()));
            //log.info("the args of this api: " + Arrays.toString(proceedingJoinPoint.getArgs()));
        }
        // 打印请求的 IP
        if (apiLogProperties.getShowIP()) {
            logMessage.append("|").append(request.getRemoteAddr()).append(":").append(request.getRemotePort());
            //log.info("IP: {}", request.getRemoteAddr());
        }
        // 打印请求头
        if(apiLogProperties.getShowHead()) {
            logMessage.append("|").append(WebContextUtil.getHeaderString(request));
        }
        Object result = proceedingJoinPoint.proceed();
        //log.info("Time-Consuming : {} ms", System.currentTimeMillis() - startTime);
        // 打印请求耗时
        logMessage.append("|").append(System.currentTimeMillis() - startTime).append("ms");
        log.info(logMessage.toString());
        return result;
    }

}
