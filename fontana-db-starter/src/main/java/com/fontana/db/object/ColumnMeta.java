package com.fontana.db.object;

import com.baomidou.mybatisplus.annotation.DbType;
import com.fontana.db.util.DbTypeUtils;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @className: ColumnMeta
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/1/13 9:24
 */
@Data
public class ColumnMeta {
    private String tableName;
    private String columnId;
    private String columnName;
    private int columnSize;
    private String colunmType;
    private String comment;
    private String fieldDefault;
    private int decimalDigits;
    private String isNullable;
    private String pkType;
    private String oldColumnName;
    private String realDbType;

    public boolean isCommentEquals(ColumnMeta columnMeta) {
        return columnMeta == this ? true : this.compareStr(this.comment, columnMeta.getComment());
    }

    private boolean compareStr(String var1, String var2) {
        boolean var3 = StringUtils.isNotEmpty(var1);
        boolean var4 = StringUtils.isNotEmpty(var2);
        if ("".equals(var2)) {
            if (!var3) {
                return true;
            } else {
                return var1.toLowerCase().contains("null");
            }
        } else if (var3 != var4) {
            return false;
        } else {
            return !var3 || var1.equals(var2);
        }
    }

    public boolean equalsByDbType(Object object, DbType dbType) {
        if (object == this) {
            return true;
        } else if (!(object instanceof ColumnMeta)) {
            return false;
        } else {
            ColumnMeta columnMeta = (ColumnMeta)object;
            if (DbTypeUtils.dbTypeIsSQLServer(dbType)) {
                if (!this.colunmType.contains("date") && !this.colunmType.contains("blob") && !this.colunmType.contains("text")) {
                    return this.colunmType.equals(columnMeta.getColunmType()) && this.isNullable.equals(columnMeta.isNullable) && this.columnSize == columnMeta.getColumnSize() && this.compareStr(this.fieldDefault, columnMeta.getFieldDefault());
                } else {
                    return this.columnName.equals(columnMeta.getColumnName()) && this.isNullable.equals(columnMeta.isNullable);
                }
            } else if (DbTypeUtils.dbTypeIsPostgre(dbType)) {
                if (!this.colunmType.contains("date") && !this.colunmType.contains("blob") && !this.colunmType.contains("text")) {
                    return this.colunmType.equals(columnMeta.getColunmType()) && this.isNullable.equals(columnMeta.isNullable) && this.columnSize == columnMeta.getColumnSize() && this.compareStr(this.fieldDefault, columnMeta.getFieldDefault());
                } else {
                    return this.columnName.equals(columnMeta.getColumnName()) && this.isNullable.equals(columnMeta.isNullable);
                }
            } else if (DbTypeUtils.dbTypeIsOracle(dbType)) {
                if (!this.colunmType.contains("date") && !this.colunmType.contains("blob") && !this.colunmType.contains("text")) {
                    return this.colunmType.equals(columnMeta.getColunmType()) && this.isNullable.equals(columnMeta.isNullable) && this.columnSize == columnMeta.getColumnSize() && this.compareStr(this.fieldDefault, columnMeta.getFieldDefault());
                } else {
                    return this.columnName.equals(columnMeta.getColumnName()) && this.isNullable.equals(columnMeta.isNullable);
                }
            } else if (!this.colunmType.contains("date") && !this.colunmType.contains("blob") && !this.colunmType.contains("text")) {
                return this.colunmType.equals(columnMeta.getColunmType()) && this.isNullable.equals(columnMeta.isNullable) && this.columnSize == columnMeta.getColumnSize() && this.compareStr(this.comment, columnMeta.getComment()) && this.compareStr(this.fieldDefault, columnMeta.getFieldDefault());
            } else {
                return this.colunmType.equals(columnMeta.getColunmType()) && this.columnName.equals(columnMeta.getColumnName()) && this.isNullable.equals(columnMeta.isNullable) && this.compareStr(this.comment, columnMeta.getComment()) && this.compareStr(this.fieldDefault, columnMeta.getFieldDefault());
            }
        }
    }
}


