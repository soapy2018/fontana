package com.bluetron.nb.common.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * Description: 
 * @author genx
 * @date 2021/6/10 11:03
 */
public class ParamUtils {

    /**
     * "1,2,3" 解析为 [1,2,3]
     *  忽略0
     * @param param String
     * @return Set<Long>
     */
    public static Set<Long> getLongSetFromString(String param) {
        if (StringUtils.isBlank(param)) {
            return new HashSet<>();
        }
        String[] ss = param.split(",");
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

    /**
     * "1,2,3" 解析为 [1,2,3]
     *  忽略0
     * @param param
     * @return
     */
    public static long[] getLongArrayFromString(String param) {
        if (StringUtils.isBlank(param)) {
            return new long[0];
        }
        String[] ss = param.split(",");

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

    public static List<Long> getLongListFromString(String param) {
        if (StringUtils.isBlank(param)) {
            return new ArrayList(0);
        }

        String[] ss = param.split(",");
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

    public static String join(long[] longArray, String separator) {
        StringBuilder sb = new StringBuilder(100);
        for (int i = 0; longArray != null && i < longArray.length; i++) {
            if (i > 0) {
                sb.append(separator);
            }
            sb.append(longArray[i]);
        }
        return sb.toString();
    }

    public static String join(Set<Long> longSet, String separator) {
        return StringUtils.join(longSet, separator);
    }


}
