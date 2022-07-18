package com.fontana.util.request;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.fontana.base.constant.AppDeviceType;
import com.fontana.base.object.TokenData;
import com.fontana.base.result.Result;
import com.fontana.util.tools.ClassUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

/**
 * 获取Servlet HttpRequest和HttpResponse的工具类。
 *
 * @author cqf
 * @date 2021-06-06
 */
@Slf4j
public class WebContextUtil extends WebUtils {

    /**
     * 判断当前是否处于HttpServletRequest上下文环境。
     *
     * @return 是返回true，否则false。
     */
    public static boolean hasRequestContext() {
        return RequestContextHolder.getRequestAttributes() != null;
    }

    /**
     * 获取Servlet请求上下文的HttpRequest对象。
     *
     * @return 请求上下文中的HttpRequest对象。
     */
    public static HttpServletRequest getHttpRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 获取Servlet请求上下文的HttpResponse对象。
     *
     * @return 请求上下文中的HttpResponse对象。
     */
    public static HttpServletResponse getHttpResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    /**
     * 判断是否ajax请求
     * spring ajax 返回含有 ResponseBody 或者 RestController注解
     *
     * @param handlerMethod HandlerMethod
     * @return 是否ajax请求
     */
    public static boolean isBody(HandlerMethod handlerMethod) {
        ResponseBody responseBody = ClassUtil.getAnnotation(handlerMethod, ResponseBody.class);
        return responseBody != null;
    }

    /**
     * 读取cookie
     *
     * @param name cookie name
     * @return cookie value
     */
    @Nullable
    public static String getCookieVal(String name) {
        HttpServletRequest request = WebContextUtil.getHttpRequest();
        Assert.notNull(request, "request from RequestContextHolder is null");
        return getCookieVal(request, name);
    }

    /**
     * 读取cookie
     *
     * @param request HttpServletRequest
     * @param name    cookie name
     * @return cookie value
     */
    @Nullable
    public static String getCookieVal(HttpServletRequest request, String name) {
        Cookie cookie = getCookie(request, name);
        return cookie != null ? cookie.getValue() : null;
    }

    /**
     * 获取所有的请求头
     *
     * @param request
     * @return
     */
    public static String getHeaderString(HttpServletRequest request) {
        StringBuilder headers = new StringBuilder();
        Enumeration<String> names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            String headerValue = request.getHeader(name);
            if (headers.length() > 0) {
                headers.append("&");
            }
            headers.append(name + "=" + headerValue);
        }
        return headers.toString();
    }

    /**
     * 获取 get的请求参数
     *
     * @param request
     * @return
     */
    public static String getParameters(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder("");
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            String value = request.getParameter(name);
            sb.append(name + "=" + value + "&");
        }
        if (sb.length() > 0) {
            sb.delete(sb.length() - 1, sb.length());
        }
        return sb.toString();
    }

    /**
     * 清除 某个指定的cookie
     *
     * @param response HttpServletResponse
     * @param key      cookie key
     */
    public static void removeCookie(HttpServletResponse response, String key) {
        setCookie(response, key, null, 0);
    }

    /**
     * 设置cookie
     *
     * @param response        HttpServletResponse
     * @param name            cookie name
     * @param value           cookie value
     * @param maxAgeInSeconds maxage
     */
    public static void setCookie(HttpServletResponse response, String name, @Nullable String value, int maxAgeInSeconds) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAgeInSeconds);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }


    /**
     * 获取请求头中的设备信息。
     *
     * @return 设备类型，具体值可参考AppDeviceType常量类。
     */
    public static int getDeviceType() {
        // 缺省都按照Web登录方式设置，如果前端header中的值为不合法值，这里也不会报错，而是使用Web缺省方式。
        int deviceType = AppDeviceType.WEB;
        String deviceTypeString = getHttpRequest().getHeader("deviceType");
        if (StrUtil.isNotBlank(deviceTypeString)) {
            Integer type = Integer.valueOf(deviceTypeString);
            if (AppDeviceType.isValid(type)) {
                deviceType = type;
            }
        }
        return deviceType;
    }

    /**
     * 将令牌对象添加到Http请求对象。
     *
     * @param tokenData 令牌对象。
     */
    public static void addTokenToRequest(TokenData tokenData) {
        HttpServletRequest request = getHttpRequest();
        request.setAttribute(TokenData.REQUEST_ATTRIBUTE_NAME, tokenData);
    }

    /**
     * 从Http Request对象中获取令牌对象。
     *
     * @return 令牌对象。
     */
    public static TokenData takeTokenFromRequest() {

        HttpServletRequest request = WebContextUtil.getHttpRequest();
        TokenData tokenData = (TokenData) request.getAttribute(TokenData.REQUEST_ATTRIBUTE_NAME);
        if (tokenData != null) {
            return tokenData;
        }
        String token = request.getHeader(TokenData.REQUEST_ATTRIBUTE_NAME);
        if (StringUtils.isNotBlank(token)) {
            tokenData = JSON.parseObject(token, TokenData.class);
        } else {
            token = request.getParameter(TokenData.REQUEST_ATTRIBUTE_NAME);
            if (StringUtils.isNotBlank(token)) {
                tokenData = JSON.parseObject(token, TokenData.class);
            }
        }
        if (tokenData != null) {
            try {
                tokenData.setShowName(URLDecoder.decode(tokenData.getShowName(), StandardCharsets.UTF_8.name()));
            } catch (UnsupportedEncodingException e) {
                log.error("Failed to call WebContextUtil.takeTokenFromRequest", e);
            }
            addTokenToRequest(tokenData);
        }
        return tokenData;
    }

    /**
     * 通过HttpServletResponse直接输出应该信息的工具方法。
     *
     * @param httpStatus     http状态码。
     * @param responseResult 应答内容。
     * @param <T>            数据对象类型。
     * @throws IOException 异常错误。
     */
    public static <T> void output(int httpStatus, Result<T> responseResult) throws IOException {
        if (httpStatus != HttpServletResponse.SC_OK) {
            log.error(JSON.toJSONString(responseResult));
        } else {
            log.info(JSON.toJSONString(responseResult));
        }
        HttpServletResponse response = getHttpResponse();
        PrintWriter out = response.getWriter();
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(httpStatus);
        if (responseResult != null) {
            out.print(JSON.toJSONString(responseResult));
        }
        out.flush();
    }

    /**
     * 通过HttpServletResponse直接输出应该信息的工具方法。
     *
     * @param httpStatus     http状态码。
     * @param <T>            数据对象类型。
     * @throws IOException 异常错误。
     */
    public static <T> void output(int httpStatus) throws IOException {
        output(httpStatus, null);
    }

    /**
     * 通过HttpServletResponse直接输出应该信息的工具方法。Http状态码为200。
     *
     * @param responseResult 应答内容。
     * @param <T>            数据对象类型。
     * @throws IOException 异常错误。
     */
    public static <T> void output(Result<T> responseResult) throws IOException {
        output(HttpServletResponse.SC_OK, responseResult);
    }

    /**
     * 私有构造函数，明确标识该常量类的作用。
     */
    private WebContextUtil() {
    }
}
