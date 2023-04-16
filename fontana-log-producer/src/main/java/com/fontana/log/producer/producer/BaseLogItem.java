package com.fontana.log.producer.producer;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author yegenchang
 * @description 单条日志结构
 * @date 2022/6/16 08:51
 */
public class BaseLogItem implements Serializable {
  private static final long serialVersionUID = -3488075856612935955L;

  public ArrayList<BaseLogContent> mContents = new ArrayList<BaseLogContent>();


  /**
   * 日志的 kv键值对
   *
   * @param key
   *            log content key
   * @param value
   *            log content value
   */
  public void pushBack(String key, String value) {
    pushBack(new BaseLogContent(key, value));
  }

  /**
   * Add a log content to the log
   *
   * @param content
   *            log content
   */
  public void pushBack(BaseLogContent content) {
    mContents.add(content);
  }

  /**
   * set log contents
   *
   * @param contents
   *            log contents
   */
  public void setLogContents(ArrayList<BaseLogContent> contents) {
    mContents =  new ArrayList<BaseLogContent>(contents);
  }


  /**
   * Get log contents
   *
   * @return log contents
   */
  public ArrayList<BaseLogContent> getLogContents() {
    return mContents;
  }


  /**
   * 输出json
   * @return
   */
  public String toJsonString() {
    JSONObject obj = new JSONObject();

    for(BaseLogContent content : mContents) {
      obj.put(content.getKey(), content.getValue());
    }
    return obj.toString();
  }

}
