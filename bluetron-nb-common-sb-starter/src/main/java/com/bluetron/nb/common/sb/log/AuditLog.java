package com.bluetron.nb.common.sb.log;

import java.lang.annotation.*;

/**
 * @author bcloud
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
