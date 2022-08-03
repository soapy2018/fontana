package com.fontana.log.producer.producer;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.fontana.log.producer.producer.errors.ProducerException;
import com.fontana.log.producer.producer.persistence.LogStoreConfig;
import com.fontana.log.producer.producer.persistence.solr.SolrLogStoreConfig;
import com.fontana.util.date.DateTimeUtil;
import com.google.common.util.concurrent.ListenableFuture;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.ExecutionException;

import static com.fontana.log.producer.producer.persistence.Constants.LOG_STORE_CONFIG_KEY_SOLR_HOST;
import static com.fontana.log.producer.producer.persistence.Constants.LOG_STORE_NAME;

/**
 * @author yegenchang
 * @description 日志推送测试
 * @date 2022/6/21 14:23
 */
public class ProducerTest {

  Producer producer;

  @Before
  public void init() {
    Map<String, String> map = new HashMap<>();
    map.put(LOG_STORE_CONFIG_KEY_SOLR_HOST, "http://192.168.8.71:8983");
    map.put(LOG_STORE_NAME,"appLog");
    LogStoreConfig storeConfig = new SolrLogStoreConfig(map);
    ProducerConfig producerConfig = new ProducerConfig();
    producer = new LogProducer(producerConfig);
    producer.putLogStoreConfig(storeConfig);
  }


  /**
   * 推送日志测试
   * @throws ProducerException
   * @throws InterruptedException
   * @throws ExecutionException
   */
  @Test
  public void pushLog() throws ProducerException, InterruptedException, ExecutionException {
    ListenableFuture<Result> future = producer.send("appLog", buildLogItem(100), (r) -> {
      Assert.assertTrue(r.isSuccessful());
    });
    Result result = future.get();
    Assert.assertTrue(result.isSuccessful());
  }

  @After
  public void unload() throws ProducerException, InterruptedException {
    producer.close();
  }


  /**
   * 构建批量日志
   *
   * @param number
   * @return
   */
  private List<BaseLogItem> buildLogItem(int number) {
    if (number <= 0) {
      number = 1;
    }
    List<BaseLogItem> arrayList = new ArrayList<>();
    for (int i = 0; i < number; i++) {
      BaseLogItem item = new BaseLogItem();
      item.pushBack(new BaseLogContent("level", "INFO"));
      item.pushBack(new BaseLogContent("logTime", DateTimeUtil.formatDate(new Date())));
      item.pushBack(new BaseLogContent("thread", "main"));
      item.pushBack(new BaseLogContent("location",
          "com.bluetron.app.middle.log.producer.LogbackTest.infoTest(LogbackTest.java:24)"));
      item.pushBack(new BaseLogContent("host", "192.168.31.179"));
      item.pushBack(new BaseLogContent("hostName", "Mac-Studio.local"));
      item.pushBack(new BaseLogContent("message", "日志单元测试  " + System.currentTimeMillis()));
      arrayList.add(item);
    }
    return arrayList;
  }
}
