package com.fontana.log.solarLog;

import com.alibaba.fastjson.JSON;
import com.bluetron.common.log.producer.producer.PerformanceLog;
import com.fontana.base.constant.CommonConstants;
import com.fontana.base.constant.HttpConstants;
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
    //PerformanceLog log
    Logger performanceLogger = LoggerFactory.getLogger("performanceLog");

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
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = attributes.getRequest();
        PerformanceLog performanceLog = buildPerformLog(proceedingJoinPoint);
        Object result = proceedingJoinPoint.proceed();
        performanceLog.setRequestTime(System.currentTimeMillis() - startTime);
        performanceLogger.info(JSON.toJSONString(performanceLog));
        return result;
    }


    /**
     * 构建性能日志日志
     *
     * @return
     */
    private PerformanceLog buildPerformLog(JoinPoint joinPoint) {

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();


        PerformanceLog log = new PerformanceLog();
        log.setHeader(WebContextUtil.getHeaderString(request));
        log.setPath(request.getRequestURL().toString());
        log.setMethod(methodSignature.getMethod().toString());
        log.setRemoteIP(request.getRemoteAddr());
        log.setTenantId(request.getHeader(HttpConstants.TENANT_ID_HEADER));
        log.setFactoryId(request.getHeader(HttpConstants.FACTORY_ID_HEADER));
        log.setUserId(request.getHeader(HttpConstants.USER_ID_HEADER));
        log.setUserName(request.getHeader(HttpConstants.USER_NAME_HEADER));
        log.setParameter(WebContextUtil.getParameters(request));
        return log;
    }

}
