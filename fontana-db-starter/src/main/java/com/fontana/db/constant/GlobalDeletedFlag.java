package com.fontana.db.constant;

/**
 * 数据记录逻辑删除标记常量。
 *
 * @author cqf
 * @date 2021-06-06
 */
public final class GlobalDeletedFlag {

    /**
     * 表示数据表记录已经删除
     */
    public static final int DELETED = 1;
    /**
     * 数据记录正常
     */
    public static final int NORMAL = 0;

    /**
     * 私有构造函数，明确标识该常量类的作用。
     */
    private GlobalDeletedFlag() {
    }
}
