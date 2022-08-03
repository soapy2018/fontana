package com.fontana.log.producer.producer.persistence;


import com.fontana.log.producer.producer.PutLogsRequest;
import com.fontana.log.producer.producer.PutLogsResponse;

/**
 * @author yegenchang
 * @description
 * @date 2022/6/14 10:10
 */
public interface LogStoreClient {


  /**
   * 写入日志
   * @param request
   * @return
   */
  PutLogsResponse putLogs(PutLogsRequest request);
}
