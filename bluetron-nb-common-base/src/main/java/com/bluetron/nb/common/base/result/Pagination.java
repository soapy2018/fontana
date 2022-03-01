package com.bluetron.nb.common.base.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhuerwei
 * @date 2021/3/19 15:48
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pagination<T> {

    protected Long page;

    private Long pageSize;

    private Long total;

    private List<T> dataList;

    public Pagination(long page, long pageSize, long total) {
        this.page = page;
        this.pageSize = pageSize;
        this.total = total;
    }

    public Pagination(List<T> dataList, long total) {
        this.total = total;
        this.dataList = dataList;
    }


    /**
     * 为了保持前端的数据格式兼容性，在没有数据的时候，需要返回空分页对象。
     * @return 空分页对象。
     */
    public static <T> Pagination<T> emptyPageData() {
        return new Pagination<>(0L, 0L,0L,new LinkedList<>());
    }

}
