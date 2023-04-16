package com.fontana.util.tools;

import org.apache.commons.lang3.StringUtils;

/**
 * @author zhuerwei
 * @date 2021/3/5 15:19
 */
public abstract class SpecialCharUtil {

    public static String dealSpecialChar(String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        return str
                .replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
    }

    public static String parseLikeSpecialChar(String pattern) {
        if (StringUtils.isBlank(pattern)) {
            return null;
        }
        return "%" + dealSpecialChar(pattern) + "%";
    }

}
