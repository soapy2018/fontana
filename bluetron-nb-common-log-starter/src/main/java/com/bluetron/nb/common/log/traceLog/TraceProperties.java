package com.bluetron.nb.common.log.traceLog;

import com.bluetron.nb.common.base.constant.CommonConstants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 日志链路追踪配置
 *
 * @author cqf
 * @date 2021/8/13
 */
@Setter
@Getter
@ConfigurationProperties(prefix = CommonConstants.TRACELOG_PREFIX)
@RefreshScope
public class TraceProperties {
    /**
     * 是否开启日志链路追踪
     */
    private Boolean enable = false;
}
