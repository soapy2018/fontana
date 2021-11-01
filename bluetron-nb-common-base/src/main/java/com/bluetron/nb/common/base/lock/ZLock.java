package com.bluetron.nb.common.base.lock;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 锁对象抽象
 *
 * @author bcloud
 * @date 2020/7/28
 * <p>
 * 
 
 */
@AllArgsConstructor
public class ZLock implements AutoCloseable {
    @Getter
    private final Object lock;

    private final DistributedLock locker;

    @Override
    public void close() throws Exception {
        locker.unlock(lock);
    }
}
