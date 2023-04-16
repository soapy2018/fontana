package com.fontana.log.producer.producer.internals;

import com.fontana.log.producer.producer.BaseLogContent;
import com.fontana.log.producer.producer.BaseLogItem;

import java.util.List;

public abstract class LogSizeCalculator {

  public static int calculate(BaseLogItem logItem) {
    int sizeInBytes = 4;
    for (BaseLogContent content : logItem.getLogContents()) {
      if (content.mKey != null) {
        sizeInBytes += content.mKey.length();
      }
      if (content.mValue != null) {
        sizeInBytes += content.mValue.length();
      }
    }
    return sizeInBytes;
  }

  public static int calculate(List<BaseLogItem> logItems) {
    int sizeInBytes = 0;
    for (BaseLogItem logItem : logItems) {
      sizeInBytes += calculate(logItem);
    }
    return sizeInBytes;
  }
}
