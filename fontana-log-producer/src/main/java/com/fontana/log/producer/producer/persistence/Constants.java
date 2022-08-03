package com.fontana.log.producer.producer.persistence;

/**
 * @author yegenchang
 * @description
 * @date 2022/6/14 10:31
 */
public class Constants {

  /**
   * solr url配置的key
   */
  public static final String LOG_STORE_CONFIG_KEY_SOLR_HOST="solr_host";

  /**
   * 日志库名称
   */
  public static final String LOG_STORE_NAME="log_store";


  /**
   * app日志库名称
   */
  public static final String APP_LOG_STORE_NAME="appLog";

  /**
   * 请求日志库
   */
  public static final String REQUEST_LOG_STORE_NAME="performanceLog";

  /**
   * 默认日志服务器地址
   */
  public static final String DEFAULT_LOG_SERV="http://127.0.0.1:8983";

}
