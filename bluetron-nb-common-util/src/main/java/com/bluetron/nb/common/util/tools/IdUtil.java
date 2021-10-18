package com.bluetron.nb.common.util.tools;

/**
 * 唯一ID生成器
 * @author wuwenli
 *
 */
public class IdUtil {

    /**
     * 生成的UUID是带-的字符串，类似于：a5c8a5e8-df2b-4706-bea4-08d0939410e3
     * @return
     */
    public static String randomUUID() {
        return cn.hutool.core.util.IdUtil.fastUUID();
    }
    
    /**
     * 生成的是不带-的字符串，类似于：b17f24ff026d40949c85a24f4f375d42
     * @return
     */
    public static String simpleUUID() {
        return cn.hutool.core.util.IdUtil.fastSimpleUUID();
    }
    
}
