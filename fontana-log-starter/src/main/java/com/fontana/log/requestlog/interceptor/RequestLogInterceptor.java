//package com.fontana.log.requestlog.interceptor;
//
//import com.alibaba.fastjson.JSON;
//import com.fontana.log.producer.producer.RequestLog;
//import com.fontana.log.requestlog.config.Constant;
//import com.fontana.log.requestlog.config.RequestWrapper;
//import com.fontana.log.requestlog.utils.HttpServletRequestUtil;
//import com.fontana.log.requestlog.utils.IPUtils;
//import com.fontana.log.producer.producer.requestLog;
//import org.slf4j.Logger;
////import org.slf4j.LoggerFactory;
//import org.slf4j.MDC;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.HttpMethod;
//import org.springframework.lang.Nullable;
//import org.springframework.util.StringUtils;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.util.ContentCachingRequestWrapper;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.UUID;
//
///**
// * @author yegenchang
// * @description 请求日志拦截器
// * @date 2022/6/20 13:52
// */
//public class RequestLogInterceptor implements HandlerInterceptor {
//
//  private static final String TRACE_ID = "traceId";
//  private Logger log = LoggerFactory.getLogger(RequestLogInterceptor.class);
//  /**
//   * 性能日志的域对象key
//   */
//  private static final String REQUEST_ATTRIBUTE = "requestLog";
//
//  /**
//   * 开始请求时间
//   */
//  private static final String REQUEST_BEGIN_TIME = "beginRequestTime";
//
//  //性能日志专用logger
//  private Logger requestLogger = LoggerFactory.getLogger("requestLog");
//
//  @Override
//  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
//      throws Exception {
//    String traceId = request.getHeader("traceId");
//    if (StringUtils.isEmpty(traceId)) {
//      traceId = UUID.randomUUID().toString().replace("-", "");
//    }
//    log.info("traceId :" + traceId);
//    MDC.put(TRACE_ID, traceId);
//    log.info("进入 RequestLogInterceptor" + request.getRequestURI());
//    try {
//      RequestLog log = createLogEntity(request);
//      request.setAttribute(REQUEST_ATTRIBUTE, log);
//      request.setAttribute(REQUEST_BEGIN_TIME, System.currentTimeMillis());
//    } catch (Exception e) {
//      log.error("生成请求日志异常", e);
//    }
//    return true;
//  }
//
//  @Override
//  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
//
//    String method = request.getMethod();
//    if (method.toString().equals(HttpMethod.GET.toString())) {
//    } else {
////      ContentCachingRequestWrapper wrapper = new ContentCachingRequestWrapper(request);
////      String body = new String(wrapper.getContentAsByteArray());
//      String body = new RequestWrapper(request).getBody();
//      String pp = request.getParameter("name");
//    }
//
//  }
//
//  @Override
//  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
//      Object handler, Exception ex) throws Exception {
//    try {
//      RequestLog log = (RequestLog) request.getAttribute(REQUEST_ATTRIBUTE);
//      if (log != null) {
//        Long beginTime = (Long) request.getAttribute(REQUEST_BEGIN_TIME);
//        if (beginTime != null) {
//          log.setRequestTime(System.currentTimeMillis() - beginTime);
//        }
//        log.setStatusCode(response.getStatus());
//        String method = request.getMethod();
//        if (method.toString().equals(HttpMethod.GET.toString())) {
//          log.setParameter(HttpServletRequestUtil.getParameters(request));
//        } else {
//          String body = new RequestWrapper(request).getBody();
//          log.setParameter(body);
//        }
//
//        //获取返回结果
//        Object result = request.getAttribute("response");
//        if (result != null) {
//          log.setResponse(result.toString());
//        }
//        requestLogger.info(JSON.toJSONString(log));
//      }
//    } catch (Exception e) {
//      log.error("推送请求日志异常", e);
//    } finally {
//      try {
//        MDC.remove(TRACE_ID);
//      } catch (Exception ee) {
//      }
//    }
//  }
//
//  /**
//   * 构建日志信息
//   *
//   * @param request
//   * @return
//   */
//  private RequestLog createLogEntity(HttpServletRequest request) {
//    RequestLog log = new RequestLog();
//    log.setHeader(HttpServletRequestUtil.getHeaderString(request));
//    log.setPath(request.getRequestURI().toString());
//    log.setMethod(request.getMethod());
//    log.setRequestTime(System.currentTimeMillis());
//    log.setRemoteIP(IPUtils.getIPAddr(request));
//    log.setTenantId(request.getHeader(Constant.TENANT_ID_HEADER));
//    log.setFactoryId(request.getHeader(Constant.FACTORY_ID_HEADER));
//    SimpleUser userInfo = HttpServletRequestUtil.getUserInfo(request);
//    if (userInfo != null) {
//      log.setUserId(userInfo.getId());
//      log.setUserName(userInfo.getUserName());
//    }
////    String method = request.getMethod();
////    if (method.toString().equals(HttpMethod.GET.toString())) {
////      log.setParameter(HttpServletRequestUtil.getParameters(request));
////    } else {
////      String body = new RequestWrapper(request).getBody();
////      log.setParameter(body);
////    }
//    return log;
//  }
//}
