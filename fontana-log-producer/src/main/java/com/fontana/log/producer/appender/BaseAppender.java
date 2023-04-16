package com.fontana.log.producer.appender;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.encoder.Encoder;
import com.fontana.base.constant.StringPool;
import com.fontana.log.producer.appender.builder.LogItemBuilder;
import com.fontana.log.producer.appender.handler.ILogHandler;
import com.fontana.log.producer.producer.BaseLogItem;
import com.fontana.log.producer.producer.LogProducer;
import com.fontana.log.producer.producer.Producer;
import com.fontana.log.producer.producer.ProducerConfig;
import com.fontana.log.producer.producer.errors.ProducerException;
import com.fontana.log.producer.producer.persistence.Constants;
import com.fontana.log.producer.producer.persistence.EnumStoreType;
import com.fontana.log.producer.producer.persistence.LogStoreConfig;
import com.fontana.log.producer.producer.persistence.solr.SolrLogStoreConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author yegenchang
 * @description
 * @date 2022/6/14 14:43
 */
public abstract class BaseAppender<E> extends UnsynchronizedAppenderBase<E> {

  private String project = "bluetron-log";

  private String userAgent = "logback";

  //系统编号
  private String appName = "";

  protected Encoder<E> encoder;

  protected ProducerConfig producerConfig = new ProducerConfig();
  protected EnumStoreType logStoreType = EnumStoreType.SOLR;
  protected LogStoreConfig logStoreConfig;

  protected Producer producer;

  protected String timeZone = "Asia/Shanghai";
  protected String timeFormat = "yyyy-MM-dd HH:mm:ss";
  protected DateTimeFormatter formatter;
  private String mdcFields;
  private ILogHandler handler; //日志处理类
  private boolean test;

  public boolean isTest() {
    return test;
  }

  public void setTest(boolean test) {
    this.test = test;
  }

  /**
   * spring 环境
   */
  private String profile;

  public String getProfile() {
    return profile;
  }

  public void setProfile(String profile) {
    this.profile = profile;
  }

  /**
   * log服务器地址
   */
  private String logServ;

  public String getLogServ() {
    return logServ;
  }

  public void setLogServ(String logServ) {
    this.logServ = logServ;
  }

  private ExecutorService executorService;

  private Logger logger = LoggerFactory.getLogger(BaseAppender.class);

  public BaseAppender(ILogHandler handler) {
    this.handler = handler;
    executorService = Executors.newFixedThreadPool(10);
  }

  @Override
  public void start() {
    try {
      doStart();
    } catch (Exception e) {
      logger.error("Failed to start LogAppender.", e);
       // addError("Failed to start LogAppender.", e);
    }
  }

  /**
   * 启动生产者
   */
  private void doStart() {
    producer = createProducer();
    super.start();
  }


  /**
   * 创建日志生产者
   * @return
   */
  public Producer createProducer() {

    switch (logStoreType){
      case ES:
        //todo
        break;
      default:
        logStoreConfig = buildSolrLogStoreConfig();

    }
    Producer producer = new LogProducer(producerConfig);
    producer.putLogStoreConfig(logStoreConfig);
    return producer;
  }


  /**
   * 构建配置
   * @return
   */
  private LogStoreConfig buildSolrLogStoreConfig() {
    TreeMap<String, String> map = new TreeMap<>();
    String serv = logServ;
    if (logServ == null || logServ.isEmpty() || serv.equals("logServ_IS_UNDEFINED")) {
       throw new RuntimeException("初始化appender失败 没有配置 app.log.logServ");
    }
    if (serv.endsWith(StringPool.SLASH)) {
      //防止最后以/结尾，在拼接完整路径的时候会有问题，所以要移除
      serv = serv.substring(0, serv.lastIndexOf(StringPool.SLASH));
    }
    map.put(Constants.LOG_STORE_CONFIG_KEY_SOLR_HOST, serv);
    map.put(Constants.LOG_STORE_NAME, getLogStore());
    LogStoreConfig config = new SolrLogStoreConfig(map);
    return config;
  }

  @Override
  public void stop() {
    try {
      doStop();
    } catch (Exception e) {
      addError("Failed to stop LoghubAppender.", e);
    }
  }

  private void doStop() throws InterruptedException, ProducerException {
    if (!isStarted()) {
      return;
    }
    super.stop();
    producer.close();
  }

  @Override
  public void append(E eventObject) {
    try {
      appendEvent(eventObject);
    } catch (Exception e) {
      addError("Failed to append event.", e);
    }
  }

  /**
   * 新增事件
   * @param eventObject
   */
  private void appendEvent(E eventObject) {
    //init Event Object
    if (!(eventObject instanceof LoggingEvent)) {
      return;
    }
    LoggingEvent event = (LoggingEvent) eventObject;

    List<BaseLogItem> logItems = new ArrayList<BaseLogItem>();
    BaseLogItem item = LogItemBuilder.build(event, appName);
    logItems.add(item);
    if (handler != null) {
      handler.handler(event, item);
    }

    //多线程异步执行
    try {
      producer.send(getLogStore(), logItems);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }



  public abstract String getLogStore();

  public String getTimeZone() {
    return timeZone;
  }

  public void setTimeZone(String timeZone) {
    this.timeZone = timeZone;
  }

  // **** ==- ProjectConfig -== **********************
  public String getProject() {
    return project;
  }

  public void setProject(String project) {
    this.project = project;
  }

  public String getUserAgent() {
    return userAgent;
  }

  public void setUserAgent(String userAgent) {
    this.userAgent = userAgent;
  }

  public int getTotalSizeInBytes() {
    return producerConfig.getTotalSizeInBytes();
  }

  public void setTotalSizeInBytes(int totalSizeInBytes) {
    producerConfig.setTotalSizeInBytes(totalSizeInBytes);
  }

  public long getMaxBlockMs() {
    return producerConfig.getMaxBlockMs();
  }

  public void setMaxBlockMs(long maxBlockMs) {
    producerConfig.setMaxBlockMs(maxBlockMs);
  }

  public int getIoThreadCount() {
    return producerConfig.getIoThreadCount();
  }

  public void setIoThreadCount(int ioThreadCount) {
    producerConfig.setIoThreadCount(ioThreadCount);
  }

  public int getBatchSizeThresholdInBytes() {
    return producerConfig.getBatchSizeThresholdInBytes();
  }

  public void setBatchSizeThresholdInBytes(int batchSizeThresholdInBytes) {
    producerConfig.setBatchSizeThresholdInBytes(batchSizeThresholdInBytes);
  }

  public int getBatchCountThreshold() {
    return producerConfig.getBatchCountThreshold();
  }

  public void setBatchCountThreshold(int batchCountThreshold) {
    producerConfig.setBatchCountThreshold(batchCountThreshold);
  }

  public int getLingerMs() {
    return producerConfig.getLingerMs();
  }

  public void setLingerMs(int lingerMs) {
    producerConfig.setLingerMs(lingerMs);
  }

  public int getRetries() {
    return producerConfig.getRetries();
  }

  public void setRetries(int retries) {
    producerConfig.setRetries(retries);
  }

  public int getMaxReservedAttempts() {
    return producerConfig.getMaxReservedAttempts();
  }

  public void setMaxReservedAttempts(int maxReservedAttempts) {
    producerConfig.setMaxReservedAttempts(maxReservedAttempts);
  }

  public long getBaseRetryBackoffMs() {
    return producerConfig.getBaseRetryBackoffMs();
  }

  public void setBaseRetryBackoffMs(long baseRetryBackoffMs) {
    producerConfig.setBaseRetryBackoffMs(baseRetryBackoffMs);
  }

  public long getMaxRetryBackoffMs() {
    return producerConfig.getMaxRetryBackoffMs();
  }

  public void setMaxRetryBackoffMs(long maxRetryBackoffMs) {
    producerConfig.setMaxRetryBackoffMs(maxRetryBackoffMs);
  }

  public Encoder<E> getEncoder() {
    return encoder;
  }

  public void setEncoder(Encoder<E> encoder) {
    this.encoder = encoder;
  }

  public void setMdcFields(String mdcFields) {
    this.mdcFields = mdcFields;
  }

  public String getAppName() {
    return appName;
  }

  public void setAppName(String systemCode) {
    this.appName = systemCode;
  }
}
