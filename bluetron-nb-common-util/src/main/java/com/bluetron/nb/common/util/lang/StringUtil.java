package com.bluetron.nb.common.util.lang;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * extends org.apache.commons.lang3.StringUtils
 * 后续有需要再自行扩展
 */
public class StringUtil extends StringUtils {

    private StringUtil() {

    }

    /**
     * Collection转字符串，默认逗号分隔
     * ["aaa","bbb"] -> aaa,bbb
     *
     * @param Collection
     * @return aaa, bbb, ccc
     */
    public static String joinIgnoreNull(Collection<String> Collection) {
        return joinIgnoreNull(Collection, ",");
    }

    /**
     * Collection转字符串
     * ["aaa","bbb"] -> aaa,bbb
     *
     * @param Collection
     * @param delimiter  连接符号
     * @return aaa, bbb, ccc
     */
    public static String joinIgnoreNull(Collection<String> Collection, CharSequence delimiter) {

        if (null == Collection || Collection.isEmpty()) {
            return "";
        }

        String joinString = Collection.stream().filter(t -> null != t)
                .collect(Collectors.joining(delimiter));
        return joinString;
    }

    /**
     * Collection转字符串
     * ["aaa","bbb"] -> aaa,bbb,但是不支持过滤null
     *
     * @param collection
     * @param separator  连接符号
     * @return aaa, bbb, ccc
     */
    public static String join(Collection<?> collection, String separator) {
        return org.apache.commons.lang3.StringUtils.join(collection, separator);
    }

    public static String join(Collection<?> collection) {
        return join(collection, ",");
    }

    /**
     * 数组转字符串
     * [1,2,3] -> 1,2,3
     *
     * @param objectsArray
     * @param separator    连接符号
     * @return aaa, bbb, ccc
     */

    public static String join(Object[] objectsArray, char separator) {
        return org.apache.commons.lang3.StringUtils.join(objectsArray, separator);
    }

    public static String join(Object[] objectsArray) {
        return join(objectsArray, ',');
    }

    /**
     * "1,2,3" 解析为 [1,2,3]
     * 忽略0
     *
     * @param param String
     * @return Set<Long>
     */
    public static Set<Long> getLongSetFromString(String param, String separator) {
        if (org.apache.commons.lang3.StringUtils.isBlank(param)) {
            return new HashSet<>();
        }
        String[] ss = param.split(separator);
        Set<Long> result = new HashSet<>(ss.length);
        long num;
        for (String s : ss) {
            num = NumberUtils.toLong(s);
            if (num > 0) {
                result.add(num);
            }
        }
        return result;
    }

    public static Set<Long> getLongSetFromString(String param) {
        return getLongSetFromString(param, ",");
    }

    /**
     * "1,2,3" 解析为 [1,2,3]
     * 忽略0
     *
     * @param param
     * @return
     */
    public static long[] getLongArrayFromString(String param, String separator) {
        if (org.apache.commons.lang3.StringUtils.isBlank(param)) {
            return new long[0];
        }
        String[] ss = param.split(separator);

        long[] result = new long[ss.length];
        long num;
        int i = 0;
        for (String s : ss) {
            num = NumberUtils.toLong(s);
            if (num > 0) {
                result[i++] = num;
            }
        }
        if (i == 0) {
            return new long[0];
        }
        return Arrays.copyOf(result, i);
    }

    public static long[] getLongArrayFromString(String param) {
        return getLongArrayFromString(param, ",");
    }


    public static List<Long> getLongListFromString(String param, String separator) {
        if (org.apache.commons.lang3.StringUtils.isBlank(param)) {
            return new ArrayList(0);
        }

        String[] ss = param.split(separator);
        List<Long> result = new ArrayList(ss.length);
        long num;
        int i = 0;
        for (String s : ss) {
            num = NumberUtils.toLong(s);
            if (num > 0) {
                result.add(num);
            }
        }
        return result;
    }

    public static List<Long> getLongListFromString(String param) {
        return getLongListFromString(param, ",");
    }
}
