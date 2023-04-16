package com.fontana.demo.feign.config;

import com.fontana.sb.config.DefaultWebMvcConfig;
import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @className: MyWebMvcConfig
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2021/12/29 13:44
 */
@Configuration
public class FeignWebMvcConfig extends DefaultWebMvcConfig {

//    openFeign虽然提供了日志增强功能，但是默认是不显示任何日志的，不过开发者在调试阶段可以自己配置日志的级别。
//    openFeign的日志级别如下：
//    NONE：默认的，不显示任何日志;
//    BASIC：仅记录请求方法、URL、响应状态码及执行时间;
//    HEADERS：除了BASIC中定义的信息之外，还有请求和响应的头信息;
//    FULL：除了HEADERS中定义的信息之外，还有请求和响应的正文及元数据。
//
//    配置起来也很简单，步骤如下：
//  1、配置类中配置日志级别
//  2、yaml文件中设置接口日志级别

//    @Bean
//    Logger.Level feignLoggerLevel(){
//        return Logger.Level.FULL;
//    }
}


