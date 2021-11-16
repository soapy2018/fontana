package com.bluetron.nb.common.util.tools;

import cn.hutool.core.bean.copier.CopyOptions;

import java.util.HashMap;
import java.util.Map;

/**
 * Bean 对象操作，先主要用于 数值copy和转换
 * <p>
 * move BeanMapper to this class
 * <p>
 * 先设置几个比较经典的方法
 *
 * @return
 */
public class BeanUtil {

    private BeanUtil() {

    }

    /**
     * bean 转为 Map，值对象为Object
     *
     * @param object
     * @return
     */
    public static Map<String, Object> beanToMapObject(Object object) {
        return cn.hutool.core.bean.BeanUtil.beanToMap(object, false, true);
    }

    /**
     * bean 转为 Map，值对象为String
     *
     * @param object
     * @return
     */
    public static Map<String, String> beanToMapString(Object object) {

        Map<String, Object> map = cn.hutool.core.bean.BeanUtil.beanToMap(object, false, true);
        Map<String, String> returnMap = new HashMap<String, String>();

        for (Map.Entry<String, Object> mapping : map.entrySet()) {
            returnMap.put(mapping.getKey(), mapping.getValue().toString());
        }

        return returnMap;
    }

    /**
     * 复制Bean对象属性，默认不忽略null值，全copy
     *
     * @param source 源Bean对象
     * @param target 目标Bean对象
     */
    public static void copyProperties(Object source, Object target) {
        cn.hutool.core.bean.BeanUtil.copyProperties(source, target);
    }

    /**
     * 复制Bean对象属性
     *
     * @param source     源Bean对象
     * @param target     目标Bean对象
     * @param ignoreNull 是否忽略null值
     */
    public static void copyProperties(Object source, Object target, Boolean ignoreNull) {
        cn.hutool.core.bean.BeanUtil.copyProperties(source, target, CopyOptions.create().setIgnoreNullValue(ignoreNull));
    }

    /**
     * 复制Bean对象属性<br>
     * 限制类用于限制拷贝的属性，例如一个类我只想复制其父类的一些属性，就可以将editable设置为父类
     *
     * @param source           源Bean对象
     * @param target           目标Bean对象
     * @param ignoreProperties 不拷贝的的属性列表
     */
    public static void copyProperties(Object source, Object target, String... ignoreProperties) {
        cn.hutool.core.bean.BeanUtil.copyProperties(source, target, ignoreProperties);
    }

}
