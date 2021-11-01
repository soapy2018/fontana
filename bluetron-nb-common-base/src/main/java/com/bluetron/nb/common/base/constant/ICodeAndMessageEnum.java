package com.bluetron.nb.common.base.constant;



/**
 * 抽象枚举类 code 和 中文名 的关系
 * enum请求参数解析， 尝试解析中文
 *
 * @author genx
 * @date 2021/4/12 13:14
 */
public interface ICodeAndMessageEnum {

    /**
     * code
     * @return
     */
    Integer getCode();

    /**
     * 中文名
     * @return
     */
    String getMessage();

    String name();
}
