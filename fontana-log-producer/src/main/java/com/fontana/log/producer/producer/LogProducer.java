package com.fontana.log.producer.producer;

import com.fontana.log.producer.producer.errors.MaxBatchCountExceedException;
import com.fontana.log.producer.producer.errors.ProducerException;
import com.fontana.log.producer.producer.internals.*;
import com.fontana.log.producer.producer.persistence.LogStoreClient;
import com.fontana.log.producer.producer.persistence.LogStoreClientFactory;
import com.fontana.log.producer.producer.persistence.LogStoreConfig;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 日志推送
 */
public class LogProducer implements Producer {

  private static final Logger LOGGER = LoggerFactory.getLogger(LogProducer.class);

  private static final AtomicInteger INSTANCE_ID_GENERATOR = new AtomicInteger(0);

  private static final String LOG_PRODUCER_PREFIX = "bluetron-log-producer-";

  private static final String MOVER_SUFFIX = "-mover";

  private static final String SUCCESS_BATCH_HANDLER_SUFFIX = "-success-batch-handler";

  private static final String FAILURE_BATCH_HANDLER_SUFFIX = "-failure-batch-handler";

  private static final String TIMEOUT_THREAD_SUFFIX_FORMAT = "-timeout-thread-%d";

  private final int instanceId;

  private final String name;

  private final String producerHash;

  private final ProducerConfig producerConfig;

  private final Map<String, LogStoreClient> clientPool = new ConcurrentHashMap<String, LogStoreClient>();


  private final Semaphore memoryController;

  private final RetryQueue retryQueue;

  private final IOThreadPool ioThreadPool;

  private final ThreadPoolExecutor timeoutThreadPool;

  private final LogAccumulator accumulator;

  private final Mover mover;

  private final BatchHandler successBatchHandler;

  private final BatchHandler failureBatchHandler;

  private final AtomicInteger batchCount = new AtomicInteger(0);

  private final ShardHashAdjuster adjuster;

  /**
   * 项目名称
   */
  private String projectName;

  /**
   * Start up a LogProducer instance.
   *
   * <p>Since this creates a series of threads and data structures, it is fairly expensive. Avoid
   * creating more than one per application.
   *
   * <p>All methods in LogProducer are thread-safe.
   *
   * @param producerConfig Configuration for the LogProducer. See the docs for that class for
   *     details.
   * @see ProducerConfig
   */
  public LogProducer(ProducerConfig producerConfig) {
    this.instanceId = INSTANCE_ID_GENERATOR.getAndIncrement();
    this.name = LOG_PRODUCER_PREFIX + this.instanceId;
    this.producerHash = Utils.generateProducerHash(this.instanceId);
    this.producerConfig = producerConfig;
    this.memoryController = new Semaphore(producerConfig.getTotalSizeInBytes());
    this.retryQueue = new RetryQueue();
    BlockingQueue<ProducerBatch> successQueue = new LinkedBlockingQueue<ProducerBatch>();
    BlockingQueue<ProducerBatch> failureQueue = new LinkedBlockingQueue<ProducerBatch>();
    this.ioThreadPool = new IOThreadPool(producerConfig.getIoThreadCount(), this.name);
    this.timeoutThreadPool =
        new ThreadPoolExecutor(
            producerConfig.getIoThreadCount(),
            producerConfig.getIoThreadCount(),
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(),
            new ThreadFactoryBuilder()
                .setDaemon(true)
                .setNameFormat(this.name + TIMEOUT_THREAD_SUFFIX_FORMAT)
                .build());
    this.accumulator =
        new LogAccumulator(
            this.producerHash,
            producerConfig,
            this.clientPool,
            this.memoryController,
            this.retryQueue,
            successQueue,
            failureQueue,
            this.ioThreadPool,
            this.batchCount);
    this.mover =
        new Mover(
            this.name + MOVER_SUFFIX,
            producerConfig,
            this.clientPool,
            this.accumulator,
            this.retryQueue,
            successQueue,
            failureQueue,
            this.ioThreadPool,
            this.batchCount);
    this.successBatchHandler =
        new BatchHandler(
            this.name + SUCCESS_BATCH_HANDLER_SUFFIX,
            successQueue,
            this.batchCount,
            this.memoryController);
    this.failureBatchHandler =
        new BatchHandler(
            this.name + FAILURE_BATCH_HANDLER_SUFFIX,
            failureQueue,
            this.batchCount,
            this.memoryController);
    this.mover.start();
    this.successBatchHandler.start();
    this.failureBatchHandler.start();
    this.adjuster = new ShardHashAdjuster(producerConfig.getBuckets());
  }


  /**
   * 发送日志
   * @param logStore 日志库名称
   * @param logItems 日志内容
   * @return
   * @throws InterruptedException
   * @throws ProducerException
   */
  @Override
  public ListenableFuture<Result> send(String logStore, List<BaseLogItem> logItems)
      throws InterruptedException, ProducerException {
    return send(logStore,logItems, null);
  }


  /**
   * 发送日志
   * @param logStore 日志名称
   * @param logItems 日志内容
   * @param callback 回调
   * @return
   * @throws InterruptedException
   * @throws ProducerException
   */
  @Override
  public ListenableFuture<Result> send(String logStore, List<BaseLogItem> logItems, Callback callback)
      throws InterruptedException, ProducerException {
    Utils.assertArgumentNotNullOrEmpty(projectName, "project");
    Utils.assertArgumentNotNullOrEmpty(logStore, "logStore");

    Utils.assertArgumentNotNull(logItems, "logItems");
    if (logItems.isEmpty()) {
      throw new IllegalArgumentException("logItems cannot be empty");
    }
    int count = logItems.size();
    if (count > ProducerConfig.MAX_BATCH_COUNT) {
      throw new MaxBatchCountExceedException(
          "the log list size is "
              + count
              + " which exceeds the MAX_BATCH_COUNT "
              + ProducerConfig.MAX_BATCH_COUNT);
    }
    return accumulator.append(projectName, logStore, "", "", null, logItems, callback);
  }

  /**
   * Close this producer. This method blocks until all previously submitted logs have been handled
   * and all background threads are stopped. This method is equivalent to <code>
   * close(Long.MAX_VALUE)</code>.
   *
   * <p><strong>If close() is called from {@link Callback}, a warning message will be logged and it
   * will skip join batch handler thread. We do this because the sender thread would otherwise try
   * to join itself and block forever.</strong>
   *
   * @throws InterruptedException If the thread is interrupted while blocked.
   * @throws ProducerException If the background threads is still alive.
   */
  @Override
  public void close() throws InterruptedException, ProducerException {
    close(Long.MAX_VALUE);
  }

  /**
   * This method waits up to <code>timeoutMs</code> for the producer to handle all submitted logs
   * and close all background threads.
   *
   * <p><strong>If this method is called from {@link Callback}, a warning message will be logged and
   * it will skip join batch handler thread. We do this because the sender thread would otherwise
   * try to join itself and block forever.</strong>
   *
   * @param timeoutMs The maximum time to wait for producer to handle all submitted logs and close
   *     all background threads. The value should be non-negative. Specifying a timeout of zero
   *     means do not wait for pending send requests to complete.
   * @throws IllegalArgumentException If the <code>timeoutMs</code> is negative.
   * @throws InterruptedException If the thread is interrupted while blocked.
   * @throws ProducerException If the producer is unable to handle all submitted logs or close all
   *     background threads.
   */
  @Override
  public void close(long timeoutMs) throws InterruptedException, ProducerException {
    if (timeoutMs < 0) {
      throw new IllegalArgumentException(
          "timeoutMs must be greater than or equal to 0, got " + timeoutMs);
    }
    ProducerException firstException = null;
    LOGGER.info("Closing the log producer, timeoutMs={}", timeoutMs);
    try {
      timeoutMs = closeMover(timeoutMs);
    } catch (ProducerException e) {
      firstException = e;
    }
    LOGGER.debug("After close mover, timeoutMs={}", timeoutMs);
    try {
      timeoutMs = closeIOThreadPool(timeoutMs);
    } catch (ProducerException e) {
      if (firstException == null) {
        firstException = e;
      }
    }
    LOGGER.debug("After close ioThreadPool, timeoutMs={}", timeoutMs);
    try {
      timeoutMs = closeTimeoutThreadPool(timeoutMs);
    } catch (ProducerException e) {
      if (firstException == null) {
        firstException = e;
      }
    }
    LOGGER.debug("After close timeoutThreadPool, timeoutMs={}", timeoutMs);
    try {
      timeoutMs = closeSuccessBatchHandler(timeoutMs);
    } catch (ProducerException e) {
      if (firstException == null) {
        firstException = e;
      }
    }
    LOGGER.debug("After close success batch handler, timeoutMs={}", timeoutMs);
    try {
      timeoutMs = closeFailureBatchHandler(timeoutMs);
    } catch (ProducerException e) {
      if (firstException == null) {
        firstException = e;
      }
    }
    LOGGER.debug("After close failure batch handler, timeoutMs={}", timeoutMs);
    if (firstException != null) {
      throw firstException;
    }
    LOGGER.info("The log producer has been closed");
  }

  private long closeMover(long timeoutMs) throws InterruptedException, ProducerException {
    long startMs = System.currentTimeMillis();
    accumulator.close();
    retryQueue.close();
    mover.close();
    mover.join(timeoutMs);
    if (mover.isAlive()) {
      LOGGER.warn("The mover thread is still alive");
      throw new ProducerException("the mover thread is still alive");
    }
    long nowMs = System.currentTimeMillis();
    return Math.max(0, timeoutMs - nowMs + startMs);
  }

  private long closeIOThreadPool(long timeoutMs) throws InterruptedException, ProducerException {
    long startMs = System.currentTimeMillis();
    ioThreadPool.shutdown();
    if (ioThreadPool.awaitTermination(timeoutMs, TimeUnit.MILLISECONDS)) {
      LOGGER.debug("The ioThreadPool is terminated");
    } else {
      LOGGER.warn("The ioThreadPool is not fully terminated");
      throw new ProducerException("the ioThreadPool is not fully terminated");
    }
    long nowMs = System.currentTimeMillis();
    return Math.max(0, timeoutMs - nowMs + startMs);
  }

  private long closeTimeoutThreadPool(long timeoutMs)
      throws InterruptedException, ProducerException {
    long startMs = System.currentTimeMillis();
    timeoutThreadPool.shutdown();
    if (timeoutThreadPool.awaitTermination(timeoutMs, TimeUnit.MILLISECONDS)) {
      LOGGER.debug("The timeoutThreadPool is terminated");
    } else {
      LOGGER.warn("The timeoutThreadPool is not fully terminated");
      throw new ProducerException("the timeoutThreadPool is not fully terminated");
    }
    long nowMs = System.currentTimeMillis();
    return Math.max(0, timeoutMs - nowMs + startMs);
  }

  private long closeSuccessBatchHandler(long timeoutMs)
      throws InterruptedException, ProducerException {
    long startMs = System.currentTimeMillis();
    successBatchHandler.close();
    boolean invokedFromCallback = Thread.currentThread() == this.successBatchHandler;
    if (invokedFromCallback) {
      LOGGER.warn(
          "Skip join success batch handler since you have incorrectly invoked close from the producer call-back");
      return timeoutMs;
    }
    successBatchHandler.join(timeoutMs);
    if (successBatchHandler.isAlive()) {
      LOGGER.warn("The success batch handler thread is still alive");
      throw new ProducerException("the success batch handler thread is still alive");
    }
    long nowMs = System.currentTimeMillis();
    return Math.max(0, timeoutMs - nowMs + startMs);
  }

  private long closeFailureBatchHandler(long timeoutMs)
      throws InterruptedException, ProducerException {
    long startMs = System.currentTimeMillis();
    failureBatchHandler.close();
    boolean invokedFromCallback =
        Thread.currentThread() == this.successBatchHandler
            || Thread.currentThread() == this.failureBatchHandler;
    if (invokedFromCallback) {
      LOGGER.warn(
          "Skip join failure batch handler since you have incorrectly invoked close from the producer call-back");
      return timeoutMs;
    }
    failureBatchHandler.join(timeoutMs);
    if (failureBatchHandler.isAlive()) {
      LOGGER.warn("The failure batch handler thread is still alive");
      throw new ProducerException("the failure batch handler thread is still alive");
    }
    long nowMs = System.currentTimeMillis();
    return Math.max(0, timeoutMs - nowMs + startMs);
  }

  /** @return Producer config of the producer instance. */
  @Override
  public ProducerConfig getProducerConfig() {
    return producerConfig;
  }

  /** @return Uncompleted batch count in the producer. */
  @Override
  public int getBatchCount() {
    return batchCount.get();
  }

  /** @return Available memory size in the producer. */
  @Override
  public int availableMemoryInBytes() {
    return memoryController.availablePermits();
  }

  /** Add or update a project config. */
  @Override
  public void putLogStoreConfig(LogStoreConfig logStoreConfig) {
    LogStoreClient client = LogStoreClientFactory.getClient(logStoreConfig);
    clientPool.put(logStoreConfig.getProject(), client);
    projectName = logStoreConfig.getProject();
  }

  /** Remove a project config. */
  @Override
  public void removeLogStoreConfig(LogStoreConfig logStoreConfig) {
    clientPool.remove(logStoreConfig.getProject());
  }

}
