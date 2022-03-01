package com.bluetron.nb.common.redis.lock;

import com.bluetron.nb.common.base.exception.GeneralException;
import com.bluetron.nb.common.base.lock.DistributedLock;
import com.bluetron.nb.common.base.lock.ZLock;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;

/**
 * @className: LockTest
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/2/11 10:05
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@SpringBootApplication
@Slf4j
public class LockTest {
    @Autowired
    DistributedLock locker;

    @Test
    public void testLock() throws InterruptedException {

        for(int i = 0; i<10; i++){

            Thread.sleep(2000);
            int num = i;
            new Thread(
                    ()-> {
                        try (
                                ZLock lock = locker.tryLock("lockKey3", 10, TimeUnit.SECONDS)
                        ) {

                            if (lock != null) {

                                // 此处是业务代码
                                log.info("进入业务代码" + num);
                                Thread.sleep(5000);
                                log.info("出业务代码" + num);
                                //try-resources模式下会自动关闭资源，无需手工关闭locker.unlock(lock);

                            } else {
                                log.info("锁等待超时" + num);
                                throw new GeneralException("锁等待超时");

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
            ).start();
        }
        Thread.sleep(30000);
    }

}


