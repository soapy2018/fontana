package com.fontana.log.producer.producer.persistence.solr;

import com.fontana.log.producer.producer.persistence.EnumStoreType;
import com.fontana.log.producer.producer.persistence.LogStoreConfig;

import java.util.Map;

/**
 * @author yegenchang
 * @description
 * @date 2022/6/14 09:52
 */
public class SolrLogStoreConfig implements LogStoreConfig {

  /**
   * 配置
   */
  private Map<String,String> config;

  public SolrLogStoreConfig(Map<String, String> map){
    this.config=map;
  }


  @Override
  public EnumStoreType type() {
    return EnumStoreType.SOLR;
  }

  @Override
  public Map<String, String> config() {
    return config;
  }
}
