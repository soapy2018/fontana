package com.bluetron.nb.common.log.auditLog;

import com.bluetron.nb.common.base.constant.CommonConstants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 审计日志配置
 *
 * @author cqf
 * @date 2020/2/3
 * <p>
 */
@Setter
@Getter
@ConfigurationProperties(prefix = CommonConstants.AUDITLOG_PREFIX)
@RefreshScope
public class AuditLogProperties {
    /**
     * 是否开启审计日志
     */
    private Boolean enabled = true;
    /**
     * 日志记录类型(logger/redis/db/es)
     */
    private String type;
}
