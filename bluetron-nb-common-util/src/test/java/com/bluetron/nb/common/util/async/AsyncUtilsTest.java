package com.bluetron.nb.common.util.async;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Random;

@Slf4j
public class AsyncUtilsTest {

	@Test
	public void testAsyncCall() {

		log.info("before async call");

		AsyncUtils.run(() -> {
			try {
				log.info("call some client API");
				Thread.sleep(new Random().nextInt(3000)); // make the call return after 3 seconds
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			return "ok";

		}).thenAccept(result -> {
			log.info("end >> client API return {} ", result);
		});

		log.info("after async call but before client API return");

		/**
		 * the log looks like this
		 * 
		 * 15:35:02.453 [main] INFO com.wwl.common.AsyncUtilsTest - before async call
		 * 15:35:02.514 [pool-1-thread-1] INFO com.wwl.common.AsyncUtilsTest - call some client API 
		 * 15:35:02.514 [main] INFO com.wwl.common.AsyncUtilsTest - after async call but before client API return 
		 * 15:35:03.773 [pool-1-thread-1] INFO com.wwl.common.AsyncUtilsTest - end >> client API return ok
		 */

	}
	
	@Test
	public void testAsyncCallThenApply() {

		String result = AsyncUtils.run(()->{
			try {
				log.info("call some client API");
				Thread.sleep(new Random().nextInt(3000)); // make the call return after 3 seconds
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return "hello";
		}).thenApply( s-> s+" world").join();
		
		log.info("result : {} " , result);
	}

}
