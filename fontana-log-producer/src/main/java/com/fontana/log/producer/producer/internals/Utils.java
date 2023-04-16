package com.fontana.log.producer.producer.internals;

import com.fontana.util.request.IpUtil;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.util.concurrent.atomic.AtomicLong;

public abstract class Utils {

  private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

  public static void assertArgumentNotNull(Object argument, String argumentName) {
    if (argument == null) {
      throw new IllegalArgumentException(argumentName + " cannot be null");
    }
  }

  public static void assertArgumentNotNullOrEmpty(String argument, String argumentName) {
    assertArgumentNotNull(argument, argumentName);
    if (argument.isEmpty()) {
      throw new IllegalArgumentException(argumentName + " cannot be empty");
    }
  }

  public static String generateProducerHash(int instanceId) {
    String ip = IpUtil.getFirstLocalIpAddress();
    if (ip == null) {
      LOGGER.warn("Failed to get local machine ip, set ip to 127.0.0.1");
      ip = "127.0.0.1";
    }
    String name = ManagementFactory.getRuntimeMXBean().getName();
    String input = ip + "-" + name + "-" + instanceId;
    return Hashing.farmHashFingerprint64().hashString(input, Charsets.US_ASCII).toString();
  }

  public static String generatePackageId(String producerHash, AtomicLong batchId) {
    return producerHash + "-" + Long.toHexString(batchId.getAndIncrement());
  }
}
