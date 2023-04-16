package com.fontana.log.producer.producer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yegenchang
 * @description
 * @date 2022/6/16 15:15
 */
public class PutLogsRequest extends Request {

  private static final long serialVersionUID = 7226856831224917838L;
  private String mLogStore;
  private String mTopic;
  private String mSource;
  private String mHashKey;
  private ArrayList<BaseLogItem> mlogItems;
  private byte[] mLogGroupBytes = null;
  private Integer hashRouteKeySeqId;

  /**
   * Construct a put log request
   *
   * @param project  project name
   * @param logStore log store name of the project
   * @param topic    topic name of the log store
   * @param source   source of the log
   * @param logItems log data
   * @param hashKey  hashKey
   */
  public PutLogsRequest(String project, String logStore, String topic,
      String source, List<BaseLogItem> logItems, String hashKey) {
    super(project);
    mLogStore = logStore;
    mTopic = topic;
    mSource = source;
    mlogItems = new ArrayList<BaseLogItem>(logItems);
    mHashKey = hashKey;
  }

  /**
   * Construct a put log request
   *
   * @param project  project name
   * @param logStore log store name of the project
   * @param topic    topic name of the log store
   * @param source   source of the log
   * @param logItems log data
   */
  public PutLogsRequest(String project, String logStore, String topic,
      String source, List<BaseLogItem> logItems) {
    super(project);
    mLogStore = logStore;
    mTopic = topic;
    mSource = source;
    mlogItems = new ArrayList<BaseLogItem>(logItems);
    mHashKey = null;
  }


  /**
   * Construct a put log request
   *
   * @param project  project name
   * @param logStore log store name of the project
   * @param topic    topic name of the log store
   * @param logItems log data
   */
  public PutLogsRequest(String project, String logStore, String topic,
      List<BaseLogItem> logItems) {
    super(project);
    mLogStore = logStore;
    mTopic = topic;
    mlogItems = new ArrayList<BaseLogItem>(logItems);
  }

  /**
   * Construct a put log request
   *
   * @param project       project name
   * @param logStore      log store name of the project
   * @param topic         topic name of the log store
   * @param source        source of the log
   * @param logGroupBytes Porotbuf serialized string of LogGroup
   * @param hashKey       hashKey
   */
  public PutLogsRequest(String project, String logStore, String topic,
      String source, byte[] logGroupBytes, String hashKey) {
    super(project);
    mLogStore = logStore;
    mTopic = topic;
    mSource = source;
    mLogGroupBytes = logGroupBytes;
    mHashKey = hashKey;
  }

  /**
   * Get log store
   *
   * @return log store
   */
  public String getLogStore() {
    return mLogStore;
  }

  /**
   * Set log store
   *
   * @param logStore log store name
   */
  public void setLogStore(String logStore) {
    mLogStore = logStore;
  }

  /**
   * Get log source
   *
   * @return log source
   */
  public String getSource() {
    return mSource;
  }

  /**
   * Set log source
   *
   * @param source log source
   */
  public void setSource(String source) {
    mSource = source;
  }

  /**
   * Get all the log data
   *
   * @return log data
   */
  public ArrayList<BaseLogItem> getLogItems() {
    return mlogItems;
  }

  /**
   * Get all the logGroupBytes
   *
   * @return logGroupBytes
   */
  public byte[] getLogGroupBytes() {
    return mLogGroupBytes;
  }

  /**
   * Set the log data , shallow copy is used to set the log data
   *
   * @param logItems log data
   */
  public void setLogItems(List<BaseLogItem> logItems) {
    mlogItems = new ArrayList<BaseLogItem>(logItems);
  }
}
