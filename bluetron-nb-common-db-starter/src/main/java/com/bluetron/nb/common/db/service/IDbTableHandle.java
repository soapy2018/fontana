package com.bluetron.nb.common.db.service;

import com.bluetron.nb.common.db.object.ColumnMeta;

import java.util.List;

/**
 * @interfaceName: DbTableHandleI
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/1/13 9:20
 */
public interface IDbTableHandle {
    String getAddColumnSql(ColumnMeta var1);

    String getReNameFieldName(ColumnMeta var1);

    String getUpdateColumnSql(ColumnMeta Column, ColumnMeta oldColumn);

    String getMatchClassTypeByDataType(String var1, int var2);

    String dropTableSQL(String var1);

    String getDropColumnSql(String var1);

    String getCommentSql(ColumnMeta var1);

    String getSpecialHandle(ColumnMeta columnMeta, ColumnMeta oldColumnMeta);

    String dropIndexs(String indexName, String tableName);

    String countIndex(String indexName, String tableName);

    default void handleUpdateMultiSql(ColumnMeta oldColumnMeta, ColumnMeta newColumnMeta, String tableName, List<String> sqlList) {
    }

}
