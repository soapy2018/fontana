package com.fontana.log.producer.producer.persistence;

import java.io.Serializable;

/**
 * @author yegenchang
 * @description
 * @date 2022/6/14 12:32
 */
public class LogStoreResponse implements Serializable {

  private String requestId;

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }
}
