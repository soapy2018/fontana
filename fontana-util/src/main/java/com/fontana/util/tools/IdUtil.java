package com.fontana.util.tools;

import cn.hutool.core.lang.Snowflake;
import com.fontana.base.constant.CommonConstants;

/**
 * 唯一ID生成器
 *
 * @author wuwenli
 */
public class IdUtil {

    private static final Snowflake snowflake = cn.hutool.core.util.IdUtil.getSnowflake(CommonConstants.WORK_NODE, 0);

    /**
     * 生成的UUID是带-的字符串，类似于：a5c8a5e8-df2b-4706-bea4-08d0939410e3
     *
     * @return
     */
    public static String randomUUID() {
        return cn.hutool.core.util.IdUtil.fastUUID();
    }

    /**
     * 生成的是不带-的字符串，类似于：b17f24ff026d40949c85a24f4f375d42
     *
     * @return
     */
    public static String simpleRandomUUID() {
        return cn.hutool.core.util.IdUtil.fastSimpleUUID();

    }

    /**
     * 获取基于Snowflake算法的数值型Id。
     * 由于底层实现为synchronized方法，因此计算过程串行化，且线程安全。
     *
     * @return 计算后的全局唯一Id。
     */

    private IdUtil(){};

    public static long nextLongId() {
        return snowflake.nextId();
    }

    /**
     * 获取基于Snowflake算法的字符串Id。
     * 由于底层实现为synchronized方法，因此计算过程串行化，且线程安全。
     *
     * @return 计算后的全局唯一Id。
     */
    public static String nextStringId() {
        return snowflake.nextIdStr();
    }

}
