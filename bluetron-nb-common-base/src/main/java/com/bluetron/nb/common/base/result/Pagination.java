package com.bluetron.nb.common.base.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    protected Long page;

    private Long pageSize;

    private Long total;

    private List<T> list;

    public Pagination(long page, long pageSize, long total) {
        this.page = page;
        this.pageSize = pageSize;
        this.total = total;
    }

}
