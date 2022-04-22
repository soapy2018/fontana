package com.bluetron.nb.common.multitenancy.aspect;

import com.bluetron.nb.common.base.constant.CommonConstants;
import com.bluetron.nb.common.base.context.TenantContextHolder;
import com.bluetron.nb.common.multitenancy.annotation.Tenant;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @className: TenantSelectorAspect
 * @Description: 创建一个AOP切面，拦截带 @Tenant 注解的类或方法，在方法执行前切换至租户，执行完成后恢复租户。
 * @version: v1.0.0
 * @author: cqf
 * @date: 2021/11/5 9:34
 */
@Aspect
@Component
@ConditionalOnProperty(prefix = CommonConstants.TENANT_PREFIX, name = "type", havingValue = "db")
@Order(0) // 该切面应当先于 @Transactional 执行
@Slf4j
public class TenantSelectorAspect {
    @Around("@within(tenant) || @annotation(tenant)")
    public Object switchTenant(ProceedingJoinPoint joinPoint, Tenant tenant) throws Exception {
        String originTenantId = TenantContextHolder.getTenant();
        try {
            if (tenant == null) {
                // 获取类上的注解
                tenant = joinPoint.getTarget().getClass().getDeclaredAnnotation(Tenant.class);
            }
            String tenantId = tenant.value();
            TenantContextHolder.setTenant(tenantId);
            log.info("Switch tenant to [{}] in Method [{}]", tenantId, joinPoint.getSignature());
        } catch (Exception e) {
            log.warn("", e);
        }
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            throw new Exception(e);
        } finally {
            TenantContextHolder.setTenant(originTenantId);
            log.info("Switch tenant to origin tenant [{}] in Method [{}]", originTenantId, joinPoint.getSignature());
        }
        return result;
    }
}


