package com.bluetron.nb.common.spring.log;

import com.bluetron.nb.common.base.constant.CommonConstants;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 *  日志拦截
 * @author chenqingfeng
 */
@Slf4j
@Aspect
@EnableConfigurationProperties({ApiLogProperties.class})
@ConditionalOnProperty(prefix = CommonConstants.APILOG_PREFIX,
		name = "enable", havingValue = "true", matchIfMissing = false)
@Configuration
public class ApiLogConfig {

	private ApiLogProperties apiLogProperties;

	public ApiLogConfig(ApiLogProperties apiLogProperties) {
		this.apiLogProperties = apiLogProperties;
	}
	
	@Pointcut("execution(public * com.bluetron..*.interfaces..*.*(..)) ||"
			+ "execution(public * com.bluetron..*.controller..*.*(..)) ||"
			+ "execution(public * cn.bluetron..*.interfaces..*.*(..)) ||"
			+ "execution(public * cn.bluetron..*.controller..*.*(..))"
	)
	public void webLog() {
	}

	@Before("webLog()")
	public void doBefore(JoinPoint joinPoint) {
		// 开始打印请求日志
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		// 打印请求 url
		log.info("{} api : {}", request.getMethod() , request.getRequestURL().toString());
		// 打印调用 controller 的全路径以及执行方法
		log.info("Class Method : {}.{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
		// 打印请求的 IP
		if(apiLogProperties.getShowip()) {
			log.info("IP: {}", request.getRemoteAddr());
		}
		// 打印请求入参
		if(apiLogProperties.getShowargs()) {
			log.info("the args of this api: "+ Arrays.toString(joinPoint.getArgs()));
		}

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
		Object result = proceedingJoinPoint.proceed();
		log.info("Time-Consuming : {} ms", System.currentTimeMillis() - startTime);
		return result;
	}

}
