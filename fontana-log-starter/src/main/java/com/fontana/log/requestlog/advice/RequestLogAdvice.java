package com.fontana.log.requestlog.advice;

import com.alibaba.fastjson.JSON;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Arrays;
import java.util.List;

/**
 * @author yegenchang
 * @description
 * @date 2022/6/20 13:38
 */
@ControllerAdvice
public class RequestLogAdvice implements ResponseBodyAdvice<Object> {

    //希望的返回值类型
    private static final List<String> acceptConvertType = Arrays.asList("org.springframework.http.converter.json.MappingJackson2HttpMessageConverter");

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        String responseStr = "";
        if (selectedContentType.toString().contains(MediaType.APPLICATION_JSON_VALUE)) {
            responseStr = JSON.toJSONString(body);
        } else if (selectedContentType.toString().contains(MediaType.TEXT_PLAIN_VALUE)) {
            responseStr = body.toString();
        } else if (selectedContentType.toString().contains(MediaType.TEXT_HTML_VALUE)) {
            responseStr = body.toString();
        } else {
            responseStr = "不支持的类型 " + selectedContentType;
        }
        ServletServerHttpRequest req = (ServletServerHttpRequest) request;
        req.getServletRequest().setAttribute("response", responseStr);
        return body;
    }
}
