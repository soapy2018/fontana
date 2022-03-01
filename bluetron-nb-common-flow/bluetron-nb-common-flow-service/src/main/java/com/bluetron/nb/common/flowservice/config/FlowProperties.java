package com.bluetron.nb.common.flowservice.config;

import com.bluetron.nb.common.base.constant.CommonConstants;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 工作流的配置对象。
 *
 * @author cqf
 * @date 2021-06-06
 */
@Data
@ConfigurationProperties(prefix = CommonConstants.COMMON_FLOW_PREFIX)
public class FlowProperties {

    /**
     * 工作落工单操作接口的URL前缀。
     */
    private String urlPrefix;
}
