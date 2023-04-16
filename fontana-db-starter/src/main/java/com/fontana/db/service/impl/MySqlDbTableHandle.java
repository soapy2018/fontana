package com.fontana.db.service.impl;

import com.fontana.db.object.ColumnMeta;
import com.fontana.db.service.IDbTableHandle;
import org.apache.commons.lang3.StringUtils;

/**
 * @className: MySqlDbTableHandle
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/1/13 13:14
 */
public class MySqlDbTableHandle implements IDbTableHandle {


    @Override
    public String getAddColumnSql(ColumnMeta columnMeta) {
        return " ADD COLUMN " + this.buildColumnSql(columnMeta) + ";";
    }

    @Override
    public String getReNameFieldName(ColumnMeta columnMeta) {
        return "CHANGE COLUMN " + columnMeta.getOldColumnName() + " " + this.buildColumnSql(columnMeta) + " ;";
    }

    @Override
    public String getUpdateColumnSql(ColumnMeta columnMeta, ColumnMeta oldColumn) {
        return " MODIFY COLUMN " + this.buildColumnSql(columnMeta) + ";";
    }

    @Override
    public String getMatchClassTypeByDataType(String dataType, int digits) {
        if (dataType.equalsIgnoreCase("varchar")) {
            return "string";
        } else if (dataType.equalsIgnoreCase("double")) {
            return "double";
        } else if (dataType.equalsIgnoreCase("int")) {
            return "int";
        } else if (dataType.equalsIgnoreCase("Date")) {
            return "date";
        } else if (dataType.equalsIgnoreCase("Datetime")) {
            return "date";
        } else if (dataType.equalsIgnoreCase("decimal")) {
            return "bigdecimal";
        } else if (dataType.equalsIgnoreCase("text")) {
            return "text";
        } else if (dataType.equalsIgnoreCase("blob")) {
            return "blob";
        }
        return null;
    }

    @Override
    public String dropTableSQL(String tableName) {
        return " DROP TABLE IF EXISTS " + tableName + " ;";
    }

    @Override
    public String getDropColumnSql(String fieldName) {
        return " DROP COLUMN " + fieldName + ";";
    }

    private String buildColumnSql(ColumnMeta columnMeta) {
        String columnSql = "";
        if (columnMeta.getColunmType().equalsIgnoreCase("string")) {
            columnSql = columnMeta.getColumnName() + " varchar(" + columnMeta.getColumnSize() + ") " + ("Y".equals(columnMeta.getIsNullable()) ? "NULL" : "NOT NULL");
        } else if (columnMeta.getColunmType().equalsIgnoreCase("date")) {
            columnSql = columnMeta.getColumnName() + " datetime " + ("Y".equals(columnMeta.getIsNullable()) ? "NULL" : "NOT NULL");
        } else if (columnMeta.getColunmType().equalsIgnoreCase("int")) {
            columnSql = columnMeta.getColumnName() + " int(" + columnMeta.getColumnSize() + ") " + ("Y".equals(columnMeta.getIsNullable()) ? "NULL" : "NOT NULL");
        } else if (columnMeta.getColunmType().equalsIgnoreCase("double")) {
            columnSql = columnMeta.getColumnName() + " double(" + columnMeta.getColumnSize() + "," + columnMeta.getDecimalDigits() + ") " + ("Y".equals(columnMeta.getIsNullable()) ? "NULL" : "NOT NULL");
        } else if (columnMeta.getColunmType().equalsIgnoreCase("bigdecimal")) {
            columnSql = columnMeta.getColumnName() + " decimal(" + columnMeta.getColumnSize() + "," + columnMeta.getDecimalDigits() + ") " + ("Y".equals(columnMeta.getIsNullable()) ? "NULL" : "NOT NULL");
        } else if (columnMeta.getColunmType().equalsIgnoreCase("text")) {
            columnSql = columnMeta.getColumnName() + " text " + ("Y".equals(columnMeta.getIsNullable()) ? "NULL" : "NOT NULL");
        } else if (columnMeta.getColunmType().equalsIgnoreCase("blob")) {
            columnSql = columnMeta.getColumnName() + " blob " + ("Y".equals(columnMeta.getIsNullable()) ? "NULL" : "NOT NULL");
        }

        columnSql = columnSql + (StringUtils.isNotEmpty(columnMeta.getComment()) ? " COMMENT '" + columnMeta.getComment() + "'" : " ");
        columnSql = columnSql + (StringUtils.isNotEmpty(columnMeta.getFieldDefault()) ? " DEFAULT " + columnMeta.getFieldDefault() : " ");
        String pkType = columnMeta.getPkType();
        if ("id".equalsIgnoreCase(columnMeta.getColumnName()) && pkType != null && ("SEQUENCE".equalsIgnoreCase(pkType) || "NATIVE".equalsIgnoreCase(pkType))) {
            columnSql = columnSql + " AUTO_INCREMENT ";
        }

        return columnSql;
    }

    @Override
    public String getCommentSql(ColumnMeta columnMeta) {
        return "";
    }

    @Override
    public String getSpecialHandle(ColumnMeta cgformcolumnMeta, ColumnMeta datacolumnMeta) {
        return null;
    }

    @Override
    public String dropIndexs(String indexName, String tableName) {
        return "DROP INDEX " + indexName + " ON " + tableName;
    }

    @Override
    public String countIndex(String indexName, String tableName) {
        return "select COUNT(*) from information_schema.statistics where table_name = '" + tableName + "'  AND index_name = '" + indexName + "'";
    }
}


