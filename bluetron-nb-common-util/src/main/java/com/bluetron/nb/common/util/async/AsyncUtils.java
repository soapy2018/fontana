package com.bluetron.nb.common.util.async;

import com.alibaba.ttl.threadpool.TtlExecutors;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import java.util.concurrent.*;

/**
 * 异步工具类
 * 
 * @author wuwl
 *
 */
public class AsyncUtils {
	
	private AsyncUtils() {
		
	}
	
	private static final ExecutorService EXECUTOR = 
	        TtlExecutors.getTtlExecutorService(new ScheduledThreadPoolExecutor( 3 ,
                    new BasicThreadFactory.Builder()
                    .namingPattern("async-schedule-pool-%d")
                    .daemon(true).build()));
	
	static {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> shutdown(EXECUTOR)));
	}
	
	public static void execute(Runnable command) {
	    EXECUTOR.execute(command);
	}
	
	public static <T> CompletableFuture<T> run(Callable<T> callable) {
        CompletableFuture<T> result = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            try {
                result.complete(callable.call());
            } catch (Throwable e) {
                result.completeExceptionally(e);
            }
        }, EXECUTOR);
        return result;
    }

	/**
	 * Shutdown as per {@link ExecutorService} Javadoc recommendation.
	 *
	 * @param executorService executor service we wish to shut down.
	 */
	private static void shutdown(ExecutorService executorService) {
		executorService.shutdown();
		try {
			if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
				executorService.shutdownNow();
				if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
					System.err.println("Thread pool did not terminate");
				}
			}
		} catch (InterruptedException ie) {
			executorService.shutdownNow();
			Thread.currentThread().interrupt();
		}
	}
}
