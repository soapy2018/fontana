package com.fontana.log.producer.producer.persistence;

import com.fontana.log.producer.producer.persistence.solr.SolrLogStoreClient;

/**
 * @author yegenchang
 * @description
 * @date 2022/6/14 10:23
 */
public class LogStoreClientFactory {


  /**
   * 获取日志操作客户端
   * @param config
   * @return
   */
  public static LogStoreClient getClient(LogStoreConfig config) {
    if (config.type().equals(EnumStoreType.SOLR)) {
        return new SolrLogStoreClient(config);
    } else {
      throw new RuntimeException("暂未支持的type" + config.type().getValue());
    }
  }
}
