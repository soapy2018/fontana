package com.bluetron.nb.common.db.property;

import com.bluetron.nb.common.base.constant.CommonConstants;
import com.zaxxer.hikari.HikariConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 日志数据源配置
 * logType=db时生效(非必须)，如果不配置则使用当前数据源
 *
 * @author cqf
 * @date 2021/9/8
 * <p>
 */
@Setter
@Getter
@ConfigurationProperties(prefix = CommonConstants.AUDITLOG_PREFIX + ".datasource")
public class LogDbProperties extends HikariConfig {
}
