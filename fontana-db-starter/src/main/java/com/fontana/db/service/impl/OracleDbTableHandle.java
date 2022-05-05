package com.fontana.db.service.impl;

import com.fontana.db.object.ColumnMeta;
import com.fontana.db.service.IDbTableHandle;
import com.fontana.util.lang.StringUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * @className: MySqlDbTableHandle
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/1/13 13:14
 */
public class OracleDbTableHandle implements IDbTableHandle {


    @Override
    public String getAddColumnSql(ColumnMeta columnMeta) {
        return " ADD  " + this.buildColumnSql(columnMeta) + "";
    }

    @Override
    public String getReNameFieldName(ColumnMeta columnMeta) {
        return "RENAME COLUMN  " + columnMeta.getOldColumnName() + " TO " + columnMeta.getColumnName() + "";
    }

    @Override
    public String getUpdateColumnSql(ColumnMeta column, ColumnMeta oldColumn) {
        return " MODIFY   " + this.buildColumnSql(column, oldColumn) + "";
    }

    @Override
    public String getMatchClassTypeByDataType(String dataType, int digits) {

        if (dataType.equalsIgnoreCase("varchar2")) {
            return "string";
        }
        if (dataType.equalsIgnoreCase("nvarchar2")) {
            return "string";
        } else if (dataType.equalsIgnoreCase("double")) {
            return "double";
        } else if (dataType.equalsIgnoreCase("number") && digits == 0) {
            return "int";
        } else if (dataType.equalsIgnoreCase("number") && digits != 0) {
            return "double";
        } else if (dataType.equalsIgnoreCase("int")) {
            return "int";
        } else if (dataType.equalsIgnoreCase("Date")) {
            return "date";
        } else if (dataType.equalsIgnoreCase("Datetime")) {
            return "date";
        } else if (dataType.equalsIgnoreCase("blob")) {
            return "blob";
        } else if (dataType.equalsIgnoreCase("clob")) {
            return "text";
        }
        return null;
    }

    @Override
    public String dropTableSQL(String tableName) {
        return " DROP TABLE  " + tableName.toLowerCase() + " ";
    }

    @Override
    public String getDropColumnSql(String fieldName) {
        return " DROP COLUMN " + fieldName.toUpperCase() + "";
    }

    private String buildColumnSql(ColumnMeta columnMeta) {

        String columnSql = "";
        if (columnMeta.getColunmType().equalsIgnoreCase("string")) {
            columnSql = columnMeta.getColumnName() + " varchar2(" + columnMeta.getColumnSize() + ")";
        } else if (columnMeta.getColunmType().equalsIgnoreCase("date")) {
            columnSql = columnMeta.getColumnName() + " date";
        } else if (columnMeta.getColunmType().equalsIgnoreCase("int")) {
            columnSql = columnMeta.getColumnName() + " NUMBER(" + columnMeta.getColumnSize() + ")";
        } else if (columnMeta.getColunmType().equalsIgnoreCase("double")) {
            columnSql = columnMeta.getColumnName() + " NUMBER(" + columnMeta.getColumnSize() + "," + columnMeta.getDecimalDigits() + ")";
        } else if (columnMeta.getColunmType().equalsIgnoreCase("bigdecimal")) {
            columnSql = columnMeta.getColumnName() + " NUMBER(" + columnMeta.getColumnSize() + "," + columnMeta.getDecimalDigits() + ")";
        } else if (columnMeta.getColunmType().equalsIgnoreCase("text")) {
            columnSql = columnMeta.getColumnName() + " CLOB ";
        } else if (columnMeta.getColunmType().equalsIgnoreCase("blob")) {
            columnSql = columnMeta.getColumnName() + " BLOB ";
        }

        columnSql = columnSql + (StringUtils.isNotEmpty(columnMeta.getFieldDefault()) ? " DEFAULT " + columnMeta.getFieldDefault() : " ");
        columnSql = columnSql + ("Y".equals(columnMeta.getIsNullable()) ? " NULL" : " NOT NULL");
        return columnSql;

    }

    private String buildColumnSql(ColumnMeta column, ColumnMeta oldColumn) {
        String columnSql = "";
        String dbType = column.getRealDbType();
        if (!oldColumn.getIsNullable().equals(column.getIsNullable())) {
            columnSql = column.getIsNullable().equals("Y") ? "NULL" : "NOT NULL";
        }

        if (column.getColunmType().equalsIgnoreCase("string")) {
            if (StringUtil.isEmpty(dbType) || !dbType.toLowerCase().contains("varchar")) {
                dbType = "varchar2";
            }

            columnSql = column.getColumnName() + " " + dbType + "(" + column.getColumnSize() + ")" + columnSql;
        } else if (column.getColunmType().equalsIgnoreCase("date")) {
            columnSql = column.getColumnName() + " date " + columnSql;
        } else if (column.getColunmType().equalsIgnoreCase("int")) {
            columnSql = column.getColumnName() + " NUMBER(" + column.getColumnSize() + ") " + columnSql;
        } else if (column.getColunmType().equalsIgnoreCase("double")) {
            columnSql = column.getColumnName() + " NUMBER(" + column.getColumnSize() + "," + column.getDecimalDigits() + ") " + columnSql;
        } else if (column.getColunmType().equalsIgnoreCase("bigdecimal")) {
            columnSql = column.getColumnName() + " NUMBER(" + column.getColumnSize() + "," + column.getDecimalDigits() + ") " + columnSql;
        } else if (column.getColunmType().equalsIgnoreCase("blob")) {
            columnSql = column.getColumnName() + " BLOB " + columnSql;
        } else if (column.getColunmType().equalsIgnoreCase("text")) {
            columnSql = column.getColumnName() + " CLOB " + columnSql;
        }

        columnSql = columnSql + (StringUtils.isNotEmpty(column.getFieldDefault()) ? " DEFAULT " + column.getFieldDefault() : " ");
        return columnSql;
    }

    @Override
    public String getCommentSql(ColumnMeta columnMeta) {
        return "COMMENT ON COLUMN " + columnMeta.getTableName() + "." + columnMeta.getColumnName() + " IS '" + columnMeta.getComment() + "'";
    }

    @Override
    public String getSpecialHandle(ColumnMeta cgformcolumnMeta, ColumnMeta datacolumnMeta) {
        return null;
    }

    @Override
    public String dropIndexs(String indexName, String tableName) {
        return "DROP INDEX " + indexName;
    }

    @Override
    public String countIndex(String indexName, String tableName) {
        return "select count(*) from user_ind_columns where index_name=upper('" + indexName + "')";
    }
}


