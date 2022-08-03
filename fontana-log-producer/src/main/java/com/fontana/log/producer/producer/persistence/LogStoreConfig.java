package com.fontana.log.producer.producer.persistence;

import java.util.Map;

/**
 * @author yegenchang
 * @description 日志存储配置
 * @date 2022/6/14 09:57
 */
public interface LogStoreConfig {

  /**
   * 项目名称
   * @return
   */
  default String getProject() {
    return "bluetron-log";
  }

  /**
   * 持久化类型 SOLR,ELASTICSEARCH
   *
   * @return
   */
  EnumStoreType type();

  /**
   * 获取配置
   *
   * @return
   */
  Map<String, String> config();
}
