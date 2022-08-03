package com.fontana.log.producer.producer;

import java.io.Serializable;

/**
 * @author yegenchang
 * @description 接口性能日志
 * @date 2022/6/17 18:42
 */
public class RequestLog implements Serializable {

  private static final long serialVersionUID = 272200203662894891L;
  /**
   * 请求路径
   */
  private String path="";

  /**
   * 请求参数
   */
  private String parameter="";

  /**
   * 接口返回值
   */
  private String response="";

  /**
   * 请求耗时
   */
  private Long requestTime = 0L;

  /**
   * 请求头对象
   */
  private String header="";

  /**
   * 响应状态码
   */
  private Integer statusCode = 200;

  /**
   * 请求方式
   */
  private String method="";

  /**
   * 当前登录用户Id
   */
  private String userId="";

  /**
   * 当前登录用户名称
   */
  private String userName="";

  /**
   * 租户编号
   */
  private String tenantId="";

  /**
   * 操作名称
   */
  private String operation="";

  /**
   * 工厂编号
   */
  private String factoryId="";

  /**
   * app的版本
   */
  private String appVersion="";

  /**
   * supos版本
   */
  private String suposVersion="";

  public String getAppVersion() {
    return appVersion;
  }

  public void setAppVersion(String appVersion) {
    this.appVersion = appVersion;
  }

  public String getSuposVersion() {
    return suposVersion;
  }

  public void setSuposVersion(String suposVersion) {
    this.suposVersion = suposVersion;
  }

  public String getFactoryId() {
    return factoryId;
  }

  public void setFactoryId(String factoryId) {
    this.factoryId = factoryId;
  }

  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
  }

  public String getOperation() {
    return operation;
  }

  public void setOperation(String operation) {
    this.operation = operation;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  /**
   * 请求的Ip
   */
  private String remoteIP;

  public String getRemoteIP() {
    return remoteIP;
  }

  public void setRemoteIP(String remoteIP) {
    this.remoteIP = remoteIP;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getParameter() {
    return parameter;
  }

  public void setParameter(String parameter) {
    this.parameter = parameter;
  }

  public String getResponse() {
    return response;
  }

  public void setResponse(String response) {
    this.response = response;
  }

  public Long getRequestTime() {
    return requestTime;
  }

  public void setRequestTime(Long requestTime) {
    this.requestTime = requestTime;
  }

  public String getHeader() {
    return header;
  }

  public void setHeader(String header) {
    this.header = header;
  }

  public Integer getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(Integer statusCode) {
    this.statusCode = statusCode;
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

}
