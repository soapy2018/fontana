package com.bluetron.nb.common.util.async;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Random;

@Slf4j
public class AsyncUtilTest {

	@Test
	public void testAsyncCall() {

		log.info("before async call");

		AsyncUtil.run(() -> {
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
		 * 15:35:02.453 [main] INFO com.wwl.common.AsyncUtilTest - before async call
		 * 15:35:02.514 [pool-1-thread-1] INFO com.wwl.common.AsyncUtilTest - call some client API 
		 * 15:35:02.514 [main] INFO com.wwl.common.AsyncUtilTest - after async call but before client API return 
		 * 15:35:03.773 [pool-1-thread-1] INFO com.wwl.common.AsyncUtilTest - end >> client API return ok
		 */

	}
	
	@Test
	public void testAsyncCallThenApply() {

		String result = AsyncUtil.run(()->{
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
