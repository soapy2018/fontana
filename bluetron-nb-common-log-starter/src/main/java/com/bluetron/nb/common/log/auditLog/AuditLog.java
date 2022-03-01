package com.bluetron.nb.common.log.auditLog;

import java.lang.annotation.*;

/**
 * @author cqf
 * @date 2020/2/3
 * <p>
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuditLog {
    /**
     * 操作信息
     */
    String operation();
}
