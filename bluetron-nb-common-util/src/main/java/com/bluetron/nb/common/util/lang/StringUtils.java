package com.bluetron.nb.common.util.lang;

import cn.hutool.core.util.StrUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * extends hutool Str
 * 后续有需要再自行扩展
 * https://hutool.cn/docs/#/core/%E5%B7%A5%E5%85%B7%E7%B1%BB/%E5%AD%97%E7%AC%A6%E4%B8%B2%E5%B7%A5%E5%85%B7-StrUtil
 *
 * @author wuwenli
 */
public class StringUtils extends StrUtil {

    private StringUtils() {

    }

    /**
     * List转字符串，默认逗号分隔
     * ["aaa","bbb"] -> aaa,bbb
     * 
     * @param list 
     * @return aaa,bbb,ccc
     */
    public static String listToString(List<String> list) {
        return StringUtils.listToString(list, ",");
    }
    
    /**
     * List转字符串
     * ["aaa","bbb"] -> aaa,bbb
     * 
     * @param list
     * @param delimiter 连接符号
     * @return aaa,bbb,ccc
     */
    public static String listToString(List<String> list , CharSequence delimiter) {
        
        if(null == list || list.isEmpty()) {
            return "";
        }
        
        String joinString = list.stream().filter(t -> null != t)
                .collect(Collectors.joining(delimiter));
        return joinString;
    }

}
