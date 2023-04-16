package com.fontana.base.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

/**
 * @author zhuerwei
 * @date 2021/3/19 15:48
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pagination<T> {

    /**
     * 数据列表。
     */
    private List<T> dataList;

    /**
     * 数据总数量。
     */
    private Long totalCount;

    public Pagination(List<T> dataList, long totalCount) {
        this.totalCount = totalCount;
        this.dataList = dataList;
    }


    /**
     * 为了保持前端的数据格式兼容性，在没有数据的时候，需要返回空分页对象。
     * @return 空分页对象。
     */
    public static <T> Pagination<T> emptyPageData() {
        return new Pagination<>(new LinkedList<>(), 0L);
    }

}
