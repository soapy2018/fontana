package com.bluetron.nb.common.redis.constant;

/**
 * redis 工具常量
 *
 * @author cqf
 * @date 2021/10/21 11:59
 */
public class RedisToolsConstant {
    /**
     * single Redis
     */
    public final static int SINGLE = 1;
    /**
     * Redis cluster
     */
    public final static int CLUSTER = 2;

    private RedisToolsConstant() {
        throw new IllegalStateException("Utility class");
    }
}
