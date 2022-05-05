package com.fontana.util.lang;

import cn.hutool.core.util.StrUtil;
import com.fontana.base.constant.StringPool;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.lang.Nullable;
import org.springframework.web.util.HtmlUtils;

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
     * 首字母变小写
     *
     * @param str 字符串
     * @return {String}
     */
    public static String lowerFirst(String str) {
        char firstChar = str.charAt(0);
        if (firstChar >= StringPool.U_A && firstChar <= StringPool.U_Z) {
            char[] arr = str.toCharArray();
            arr[0] += (StringPool.L_A - StringPool.U_A);
            return new String(arr);
        }
        return str;
    }

    /**
     * 首字母变大写
     *
     * @param str 字符串
     * @return {String}
     */
    public static String upperFirst(String str) {
        char firstChar = str.charAt(0);
        if (firstChar >= StringPool.L_A && firstChar <= StringPool.L_Z) {
            char[] arr = str.toCharArray();
            arr[0] -= (StringPool.L_A - StringPool.U_A);
            return new String(arr);
        }
        return str;
    }

    /**
     * 转义HTML用于安全过滤
     *
     * @param html html
     * @return {String}
     */
    public static String escapeHtml(String html) {
        return StringUtil.isBlank(html) ? StringPool.EMPTY : HtmlUtils.htmlEscape(html);
    }

    /**
     * 将SQL Like中的通配符替换为字符本身的含义，以便于比较。
     *
     * @param str 待替换的字符串。
     * @return 替换后的字符串。
     */
    public static String replaceSqlWildcard(String str) {
        if (StrUtil.isBlank(str)) {
            return str;
        }
        return StrUtil.replaceChars(StrUtil.replaceChars(str, "_", "\\_"), "%", "\\%");
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

    /**
     * 将驼峰命名转化成下划线
     * @param para
     * @return
     */
    public static String camelToUnderline(String para){
        if(para.length()<3){
            return para.toLowerCase();
        }
        StringBuilder sb=new StringBuilder(para);
        int temp=0;//定位
        //从第三个字符开始 避免命名不规范
        for(int i=2;i<para.length();i++){
            if(Character.isUpperCase(para.charAt(i))){
                sb.insert(i+temp, "_");
                temp+=1;
            }
        }
        return sb.toString().toLowerCase();
    }

    /**
     * 获取标识符，用于参数清理
     *
     * @param param 参数
     * @return 清理后的标识符
     */
    @Nullable
    public static String cleanIdentifier(@Nullable String param) {
        if (param == null) {
            return null;
        }
        StringBuilder paramBuilder = new StringBuilder();
        for (int i = 0; i < param.length(); i++) {
            char c = param.charAt(i);
            if (Character.isJavaIdentifierPart(c)) {
                paramBuilder.append(c);
            }
        }
        return paramBuilder.toString();
    }

}
