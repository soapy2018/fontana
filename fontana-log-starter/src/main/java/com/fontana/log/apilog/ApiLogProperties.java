package com.fontana.log.apilog;

import com.fontana.base.constant.CommonConstants;
import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @program: titanboot
 * @description: apilog相关开放的参数
 * @author: wuwenli
 * @date: 2021-08-19 13:12
 **/
@ConfigurationProperties(prefix = CommonConstants.APILOG_PREFIX)
@Data
@ToString
@RefreshScope
public class ApiLogProperties {

    /**
     * apilog是否开启
     */
    private Boolean enabled = false;

    /**
     * 记录中是否显示来源IP
     */
    private Boolean showIP = false;

    /**
     * 记录中是否显示入参
     */
    private Boolean showArgs = true;

    /**
     * 记录中是否显示请求头
     */
    private Boolean showHead = false;

    /**
     * 自定义切面表达式
     */
    private String pointcut = "execution(public * com.bluetron..*.interfaces..*.*(..)) ||"
            + "execution(public * com.bluetron..*.controller..*.*(..)) ||"
            + "execution(public * cn.bluetron..*.interfaces..*.*(..)) ||"
            + "execution(public * cn.bluetron..*.controller..*.*(..)) ||"
            + "execution(public * com.fontana..*.controller..*.*(..))"
            + "execution(public * cn.grab..*.controller..*.*(..))";

}
