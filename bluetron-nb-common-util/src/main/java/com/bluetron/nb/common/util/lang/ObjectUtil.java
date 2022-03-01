package com.bluetron.nb.common.util.lang;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Collection;

/**
 * @className: ObjectUtil
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2021/12/31 10:44
 */
public class ObjectUtil extends ObjectUtils{

    private ObjectUtil() {
    }


    /**
     * 这个方法一般用于Controller对于入口参数的基本验证。
     * 对于字符串，如果为空字符串，也将视为Blank，同时返回true。
     *
     * @param objs 一组参数。
     * @return 返回是否存在null或空字符串的参数。
     */
    public static boolean isAnyBlankOrNull(Object... objs) {
        for (Object obj : objs) {
            if (isBlankOrNull(obj)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 验证参数是否为空。
     *
     * @param obj 待判断的参数。
     * @return 空或者null返回true，否则false。
     */
    public static boolean isBlankOrNull(Object obj) {
        if (obj instanceof Collection) {
            return CollUtil.isEmpty((Collection<?>) obj);
        }
        return obj == null || (obj instanceof CharSequence && StrUtil.isBlank((CharSequence) obj));
    }

    public static Integer getInt(Object object) {
        if (isEmpty(object)) {
            return null;
        }
        try {
            return (Integer.parseInt(object.toString()));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}


