package com.bluetron.nb.common.log.apiLog;

import com.bluetron.nb.common.base.constant.CommonConstants;
import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @program: titanboot
 * @description: apilog相关开放的参数
 * @author: wuwenli
 * @date: 2021-08-19 13:12
 **/
@ConfigurationProperties(prefix = CommonConstants.APILOG_PREFIX)
@Data
@ToString
public class ApiLogProperties {

    /**
     * apilog是否开启
     */
    private Boolean enable = false;

    /**
     * 记录中是否显示来源IP
     */
    private Boolean showip = false;

    /**
     * 记录中是否显示入参
     */
    private Boolean showargs = true;

}
