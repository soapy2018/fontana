package com.fontana.log.producer.producer;

import com.fontana.log.producer.producer.errors.ProducerException;
import com.fontana.log.producer.producer.persistence.LogStoreConfig;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

/**
 * @author yegenchang
 * @description 日志推送
 * @date 2022/6/16 08:51
 */
public interface Producer {


  /**
   * 发送日志
   *
   * @param logStore 日志库名称
   * @param logItems 日志内容
   * @return
   * @throws InterruptedException
   * @throws ProducerException
   */
  ListenableFuture<Result> send(String logStore, List<BaseLogItem> logItems)
      throws InterruptedException, ProducerException;


  /**
   * 发送日志
   *
   * @param logStore
   * @param logItems
   * @param callback
   * @return
   * @throws InterruptedException
   * @throws ProducerException
   */
  ListenableFuture<Result> send(String logStore, List<BaseLogItem> logItems,
      Callback callback)
      throws InterruptedException, ProducerException;

  /**
   * 关闭资源
   *
   * @throws InterruptedException
   * @throws ProducerException
   */
  void close() throws InterruptedException, ProducerException;

  /**
   * 关闭资源
   *
   * @param timeoutMs
   * @throws InterruptedException
   * @throws ProducerException
   */
  void close(long timeoutMs) throws InterruptedException, ProducerException;


  /**
   * 生产者配置
   *
   * @return
   */
  ProducerConfig getProducerConfig();

  /**
   * 批量日志数量
   *
   * @return
   */
  int getBatchCount();

  /**
   * 获取可用内存
   *
   * @return
   */
  int availableMemoryInBytes();


  /**
   * 新增配置
   *
   * @param logStoreConfig 存储配置
   */
  void putLogStoreConfig(LogStoreConfig logStoreConfig);

  /**
   * 移除配置
   *
   * @param logStoreConfig 存储配置
   */
  void removeLogStoreConfig(LogStoreConfig logStoreConfig);
}
