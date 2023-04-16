package com.fontana.log.producer.producer.errors;

/** The logs exceeds the maximum batch count. */
public class MaxBatchCountExceedException extends ProducerException {

  public MaxBatchCountExceedException() {
    super();
  }

  public MaxBatchCountExceedException(String message, Throwable cause) {
    super(message, cause);
  }

  public MaxBatchCountExceedException(String message) {
    super(message);
  }

  public MaxBatchCountExceedException(Throwable cause) {
    super(cause);
  }
}
