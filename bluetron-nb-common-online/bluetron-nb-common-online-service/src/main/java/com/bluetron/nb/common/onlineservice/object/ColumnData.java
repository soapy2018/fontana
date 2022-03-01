package com.bluetron.nb.common.onlineservice.object;

import com.bluetron.nb.common.onlineservice.entity.OnlineColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 表字段数据对象。
 *
 * @author cqf
 * @date 2021-06-06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColumnData {

    /**
     * 在线表字段对象。
     */
    private OnlineColumn column;

    /**
     * 字段值。
     */
    private Object columnValue;
}
