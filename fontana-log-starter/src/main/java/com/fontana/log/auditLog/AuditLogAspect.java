package com.fontana.log.auditLog;

import com.fontana.base.constant.HttpConstants;
import com.fontana.base.log.Audit;
import com.fontana.base.log.IAuditService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * 审计日志切面
 *
 * @author cqf
 * @date 2021/11/3
 * <p>
 */
@Slf4j
@Aspect
@Component
@ConditionalOnClass({HttpServletRequest.class, RequestContextHolder.class, AuditLogProperties.class, IAuditService.class})
public class AuditLogAspect {
    @Value("${spring.application.name:COMMON}")
    private String applicationName;

    private AuditLogProperties auditLogProperties;
    private IAuditService auditService;
    /**
     * 用于SpEL表达式解析.
     */
    private SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
    /**
     * 用于获取方法参数定义名字.
     */
    private DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();
    public AuditLogAspect(AuditLogProperties auditLogProperties, IAuditService auditService) {
        this.auditLogProperties = auditLogProperties;
        this.auditService = auditService;
    }

    /**
     * 环绕
     *
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("@within(auditLog) || @annotation(auditLog)")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint, AuditLog auditLog) throws Throwable {

        //判断功能是否开启
        if (auditLogProperties.getEnabled()) {
            long startTime = System.currentTimeMillis();
            if (auditService == null) {
                log.warn("AuditLogAspect - auditService is null");
            }
            if (auditLog == null) {
                auditLog = proceedingJoinPoint.getTarget().getClass().getDeclaredAnnotation(AuditLog.class);
            }
            Audit audit = getAudit(auditLog, proceedingJoinPoint);
            Object result = proceedingJoinPoint.proceed();
            audit.setTimeConsume(Long.valueOf(System.currentTimeMillis() - startTime).toString()+"ms");
            auditService.save(audit);
            return result;
        }
        return proceedingJoinPoint.proceed();
    }

    /**
     * 解析spEL表达式
     */
    private String getValBySpEL(String spEL, MethodSignature methodSignature, Object[] args) {
        //获取方法形参名数组
        String[] paramNames = nameDiscoverer.getParameterNames(methodSignature.getMethod());
        if (paramNames != null && paramNames.length > 0) {
            Expression expression = spelExpressionParser.parseExpression(spEL);
            // spring的表达式上下文对象
            EvaluationContext context = new StandardEvaluationContext();
            // 给上下文赋值
            for (int i = 0; i < args.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }
            return expression.getValue(context).toString();
        }
        return null;
    }

    /**
     * 构建审计对象
     */
    private Audit getAudit(AuditLog auditLog, JoinPoint joinPoint) {
        Audit audit = new Audit();
        audit.setTimestamp(LocalDateTime.now());
        audit.setApplicationName(applicationName);

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        audit.setClassName(methodSignature.getDeclaringTypeName());
        audit.setMethodName(methodSignature.getName());

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String userId = request.getHeader(HttpConstants.USER_ID_HEADER);
        String userName = request.getHeader(HttpConstants.USER_NAME_HEADER);
        String tenantId = request.getHeader(HttpConstants.TENANT_ID_HEADER);
        audit.setUserId(userId);
        audit.setUserName(userName);
        audit.setTenantId(tenantId);

        String operation = auditLog.operation();
        if (operation.contains("#")) {
            //获取方法参数值
            Object[] args = joinPoint.getArgs();
            operation = getValBySpEL(operation, methodSignature, args);
        }
        audit.setOperation(operation);

        return audit;
    }
}
