package com.fontana.log.requestlog.filter;

import com.alibaba.fastjson.JSON;
import com.fontana.base.constant.CommonConstants;
import com.fontana.base.constant.HttpConstants;
import com.fontana.log.producer.producer.RequestLog;
import com.fontana.log.requestlog.config.RequestLogProperties;
import com.fontana.log.requestlog.config.RequestWrapper;
import com.fontana.util.request.IpUtil;
import com.fontana.util.request.WebContextUtil;
import com.fontana.util.tools.MDCTraceUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * web过滤器，生成日志链路追踪id，并赋值MDC
 * 此filter必须在{@link com.fontana.log.tracelog.WebTraceFilter} 后执行，因此order要比它大
 *
 * @author cqf
 * @date 2021/10/14
 * <p>
 */
@Component
@ConditionalOnClass(value = {HttpServletRequest.class, OncePerRequestFilter.class})
@ConditionalOnProperty(prefix = CommonConstants.REQUESTLOG_PREFIX, name = "enable", havingValue = "true")
@Order(value = MDCTraceUtil.FILTER_ORDER + 1)
@Slf4j
public class RequestLogFilter extends OncePerRequestFilter {
    //性能日志专用logger
    private final Logger requestLogger = LoggerFactory.getLogger("requestLog");
    //@Resource
    private RequestLogProperties requestLogProperties;

    public RequestLogFilter(RequestLogProperties requestLogProperties){
        this.requestLogProperties = requestLogProperties;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !(requestLogProperties == null || requestLogProperties.getEnable());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest requestWrapper = null;
//        if (request.getContentType() != null && request.getContentType()
//                .contains("multipart/form-data")) {
//            //文件上传，不能走requestWrapper，不然控制器拿不到文件了，stream被提取读了
//            filterChain.doFilter(request, response);
//            return;
//        }
        Long beginTime = System.currentTimeMillis();
        if (request instanceof HttpServletRequest) {
            requestWrapper = new RequestWrapper(request);
        }
        if (requestWrapper == null) {
            filterChain.doFilter(request, response);
        } else {
            filterChain.doFilter(requestWrapper, response);
            try {
                RequestLog log = createLogEntity(requestWrapper, response);
                log.setRequestTime(System.currentTimeMillis() - beginTime);
                requestLogger.info(JSON.toJSONString(log));
            } catch (Exception e) {
                log.error("生成请求日志异常", e);
            }

        }
    }

    /**
     * 构建日志信息
     *
     * @param request
     * @return
     */
    private RequestLog createLogEntity(HttpServletRequest request, HttpServletResponse response) {

        RequestLog log = new RequestLog();
        if(requestLogProperties == null || requestLogProperties.getShowHead()) {
            log.setHeader(WebContextUtil.getHeaderString(request));
        }
        log.setPath(request.getRequestURI());
        log.setMethod(request.getMethod());
        log.setRequestTime(System.currentTimeMillis());

        if(requestLogProperties == null || requestLogProperties.getShowIP()) {
            log.setRemoteIP(IpUtil.getRemoteIpAddress(request));
        }
        log.setTenantId(WebContextUtil.getTenantId(request));
        log.setFactoryId(WebContextUtil.getFactoryId(request));
//        SimpleUser userInfo = HttpServletRequestUtil.getUserInfo(request);
//        if (userInfo != null) {
//            log.setUserId(userInfo.getId());
//            log.setUserName(userInfo.getUserName());
//        }
        log.setUserId(WebContextUtil.getUserId(request));
        log.setUserName(WebContextUtil.getUserName(request));

        String method = request.getMethod();
        if(requestLogProperties == null || requestLogProperties.getShowArgs()) {
            if (method.equals(HttpMethod.GET.toString())) {
                log.setParameter(WebContextUtil.getParameters(request));
            } else {
                String body = new RequestWrapper(request).getBody();
                log.setParameter(body);
            }
        }
        log.setStatusCode(response.getStatus());
        //获取返回结果
        Object result = request.getAttribute("response");
        if (result != null && (requestLogProperties == null || requestLogProperties.getShowRes())) {
            log.setResponse(result.toString());
        }
        return log;
    }
}
