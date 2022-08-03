package com.fontana.log.producer.producer;

import java.io.Serializable;

/**
 * 内置kv内容
 */
public class BaseLogContent implements Serializable {
  private static final long serialVersionUID = 6042186396863898096L;
  public String mKey;
  public String mValue;

  /**
   * Construct a empty log content
   */
  public BaseLogContent() {
  }

  /**
   * Construct a log content pair
   *
   * @param key
   *            log content key
   * @param value
   *            log content value
   */
  public BaseLogContent(String key, String value) {
    this.mKey = key;
    this.mValue = value;
  }

  /**
   * Get log content key
   *
   * @return log content key
   */
  public String getKey() {
    return mKey;
  }

  /**
   * Get log content value
   *
   * @return log content value
   */
  public String getValue() {
    return mValue;
  }
}
