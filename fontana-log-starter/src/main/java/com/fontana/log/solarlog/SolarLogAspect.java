package com.fontana.log.solarlog;

import com.alibaba.fastjson.JSON;
import com.fontana.base.constant.CommonConstants;
import com.fontana.base.constant.HttpConstants;
import com.fontana.log.producer.producer.RequestLog;
import com.fontana.util.request.WebContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@ConditionalOnProperty(prefix = CommonConstants.SOLARLOG_PREFIX, name = "enable", havingValue = "true")
@Component
public class SolarLogAspect {

    //性能日志专用logger
    //requestLog log
    Logger requestLogger = LoggerFactory.getLogger("requestLog");

    private SolarLogProperties solarLogProperties;

    public SolarLogAspect(SolarLogProperties solarLogProperties) {
        this.solarLogProperties = solarLogProperties;
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
        RequestLog requestLog = buildPerformLog(proceedingJoinPoint);
        Object result = proceedingJoinPoint.proceed();
        requestLog.setRequestTime(System.currentTimeMillis() - startTime);
        requestLogger.info(JSON.toJSONString(requestLog));
        return result;
    }


    /**
     * 构建性能日志日志
     *
     * @return
     */
    private RequestLog buildPerformLog(JoinPoint joinPoint) {

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        RequestLog log = new RequestLog();
        log.setHeader(WebContextUtil.getHeaderString(request));
        log.setPath(request.getRequestURL().toString());
        log.setMethod(methodSignature.getMethod().toString());
        log.setRemoteIP(request.getRemoteAddr());
        log.setTenantId(request.getHeader(HttpConstants.TENANT_ID_HEADER));
        log.setFactoryId(request.getHeader(HttpConstants.FACTORY_ID_HEADER));
        log.setUserId(request.getHeader(HttpConstants.USER_ID_HEADER));
        log.setUserName(request.getHeader(HttpConstants.USER_NAME_HEADER));
        log.setParameter(Arrays.toString(joinPoint.getArgs()));
        return log;
    }

}
