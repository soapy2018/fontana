package com.bluetron.nb.common.util.tools;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collections;
import java.util.List;

public class CopyUtil {

    /**
     * 深度拷贝对象
     *
     * @param src
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <T> List<T> deepCopy(List<T> src) {

        //list深度拷贝
        List<T> newList = Lists.newArrayList();
        CollectionUtils.addAll(newList, new Object[src.size()]);
        Collections.copy(newList, src);
        return newList;
    }

}
