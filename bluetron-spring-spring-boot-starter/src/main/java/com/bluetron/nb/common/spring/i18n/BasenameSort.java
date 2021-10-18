package com.bluetron.nb.common.spring.i18n;

import java.util.List;

/**
 * @description: basename 排序，它的排序结果会影响冲突键覆盖的先后顺序
 * @author: Shirman
 * @date: 2020/3/30
 */
public abstract class BasenameSort {
    /**
     * 对baseNames进行排序
     * @param baseNames
     */
    protected void sort(List<String> baseNames){}
}
