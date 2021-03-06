package com.fontana.onlineservice.object;

import lombok.Data;

/**
 * 连接表信息对象。
 *
 * @author cqf
 * @date 2021-06-06
 */
@Data
public class JoinTableInfo {

    /**
     * 是否左连接。
     */
    private Boolean leftJoin;

    /**
     * 连接表表名。
     */
    private String joinTableName;

    /**
     * 连接条件。
     */
    private String joinCondition;
}
