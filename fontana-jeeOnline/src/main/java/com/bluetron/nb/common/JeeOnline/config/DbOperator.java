package com.fontana.JeeOnline.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.fontana.base.exception.GeneralException;
import com.fontana.db.object.ColumnMeta;
import com.fontana.db.service.IDbTableHandle;
import com.fontana.db.util.DbTypeUtils;
import com.fontana.db.util.JdbcUtil;
import com.fontana.JeeOnline.entity.OnlCgformField;
import com.fontana.JeeOnline.model.TableInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 * @className: DbOperator
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/1/13 11:19
 */
@Slf4j
public class DbOperator {
    private static final String tableTemplate = "com/fontana/JeeOnline/engine/tableTemplate.ftl";
    private static IDbTableHandle dbTableHandleI;

    public DbOperator(DbType dbType) throws SQLException {
        dbTableHandleI = JdbcUtil.getDbTableHandle(dbType);
    }

    private static String trans(String str) {
        if (StringUtils.isNotEmpty(str)) {
            try {
                Double.valueOf(str);
            } catch (Exception exp) {
                if (!str.startsWith("'") || !str.endsWith("'")) {
                    str = "'" + str + "'";
                }
            }
        }
        return str;
    }

    public static void createTable(TableInfo tableInfo) throws IOException, SQLException {

        DbType dbType = JdbcUtil.getDatabaseTypeEnum();

        if (DbTypeUtils.dbTypeIsOracle(dbType)) {

            ArrayList<OnlCgformField> onlCgformFieldList = new ArrayList();
            OnlCgformField onlCgformField;
            for(Iterator iterator = tableInfo.getColumns().iterator(); iterator.hasNext(); onlCgformFieldList.add(onlCgformField)) {
                onlCgformField = (OnlCgformField)iterator.next();
                if ("int".equals(onlCgformField.getDbType())) {
                    onlCgformField.setDbType("double");
                    onlCgformField.setDbPointLength(0);
                }
            }
            tableInfo.setColumns(onlCgformFieldList);
        }

        HashMap settings = new HashMap();

        settings.put("hibernate.connection.driver_class", JdbcUtil.getDriverClassName());
        settings.put("hibernate.connection.url", JdbcUtil.getUrl());
        settings.put("hibernate.connection.username", JdbcUtil.getUserName());

        settings.put("hibernate.show_sql", true);
        settings.put("hibernate.format_sql", true);
        settings.put("hibernate.temp.use_jdbc_metadata_defaults", false);
        settings.put("hibernate.dialect", DbTypeUtils.getDbDialect(dbType));
        settings.put("hibernate.hbm2ddl.auto", "create");
        settings.put("hibernate.connection.autocommit", false);
        settings.put("hibernate.current_session_context_class", "thread");

        StandardServiceRegistry standardServiceRegistry = (new StandardServiceRegistryBuilder()).applySettings(settings).build();
        MetadataSources metadataSources = new MetadataSources(standardServiceRegistry);
        String metaStr = MyMaker.maker(tableTemplate, buildDataModel(tableInfo, dbType));
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(metaStr.getBytes("utf-8"));
        metadataSources.addInputStream(byteArrayInputStream);
        Metadata metadata = metadataSources.buildMetadata();
        SchemaExport schemaExport = new SchemaExport();
        schemaExport.create(EnumSet.of(TargetType.DATABASE), metadata);
        byteArrayInputStream.close();

        List list = schemaExport.getExceptions();
        Iterator iterator = list.iterator();

        while(iterator.hasNext()) {
            Exception exception = (Exception)iterator.next();
            if ("java.sql.SQLSyntaxErrorException".equals(exception.getCause().getClass().getName())) {
                SQLSyntaxErrorException sqlSyntaxErrorException = (SQLSyntaxErrorException)exception.getCause();
                if ("42000".equals(sqlSyntaxErrorException.getSQLState())) {
                    if (1064 != sqlSyntaxErrorException.getErrorCode() && 903 != sqlSyntaxErrorException.getErrorCode()) {
                        continue;
                    }

                    log.error(sqlSyntaxErrorException.getMessage());
                    throw new GeneralException("请确认表名是否为关键字。");
                }
            } else {
                if ("com.microsoft.sqlserver.jdbc.SQLServerException".equals(exception.getCause().getClass().getName())) {
                    if (exception.getCause().toString().contains("Incorrect syntax near the keyword")) {
                        exception.printStackTrace();
                        throw new GeneralException(exception.getCause().getMessage());
                    }

                    log.error(exception.getMessage());
                    continue;
                }

                if (DbType.DM.equals(dbType) || DbType.DB2.equals(dbType)) {
                    String message = exception.getMessage();
                    if (message != null && message.contains("Error executing DDL \"drop table")) {
                        log.error(message);
                        continue;
                    }
                }
            }

            throw new GeneralException(exception.getMessage());
        }

    }

    public List<String> buildAlterTableSql(TableInfo tableInfo, DbType dbType) {

        String dbTypeString = DbTypeUtils.getDbTypeString(dbType);
        String tableName = JdbcUtil.ConvertTableNameByDbType(tableInfo.getTableName(), dbTypeString);

        String alterSql = "alter table  " + tableName + " ";
        ArrayList sqlList = new ArrayList();

        try {
            //获取db中已存在的表结构信息
            Map DbTableInfoMap = this.buildDbTableInfoMap(tableName);
            //构造新表结构信息
            Map columnMetaMap = this.buildTableFieldsMap(tableInfo);
            //新旧字段映射
            Map fieldNameMap = this.buildDbFieldNameMap(tableInfo.getColumns());
            Iterator iterator = columnMetaMap.keySet().iterator();

            while (true) {
                while (true) {
                    String fieldName;
                    while (iterator.hasNext()) {
                        fieldName = (String) iterator.next();
                        ColumnMeta columnMeta;
                        if (!DbTableInfoMap.containsKey(fieldName)) {  //数据表不存在该字段
                            columnMeta = (ColumnMeta) columnMetaMap.get(fieldName);
                            String oldFieldName = (String) fieldNameMap.get(fieldName);
                            if (fieldNameMap.containsKey(columnMeta) && DbTableInfoMap.containsKey(oldFieldName)) {
                                ColumnMeta oldColumnMeta = (ColumnMeta) DbTableInfoMap.get(oldFieldName);
                                String columnSql;
                                if (DbType.HSQL.equals(dbType)) {
                                    this.handleUpdateMultiSql(oldColumnMeta, columnMeta, tableName, sqlList);
                                } else {
                                    columnSql = dbTableHandleI.getReNameFieldName(columnMeta);
                                    if (DbTypeUtils.dbTypeIsSQLServer(dbType)) {
                                        sqlList.add(columnSql);
                                    } else {
                                        sqlList.add(alterSql + columnSql);
                                    }

                                    if (DbType.DB2.equals(dbType)) {
                                        this.handleUpdateMultiSql(oldColumnMeta, columnMeta, tableName, sqlList);
                                    } else {
                                        if (!oldColumnMeta.equals(columnMeta)) {
                                            sqlList.add(alterSql + this.getUpdateColumnSql(columnMeta, oldColumnMeta));
                                            if (DbTypeUtils.dbTypeIsPostgre(dbType)) {
                                                sqlList.add(alterSql + this.getSpecialHandle(columnMeta, oldColumnMeta));
                                            }
                                        }

                                        if (!DbTypeUtils.dbTypeIsSQLServer(dbType) && !oldColumnMeta.isCommentEquals(columnMeta)) {
                                            sqlList.add(this.getCommentSql(columnMeta));
                                        }
                                    }
                                }

                                columnSql = this.updateFiledNameOld(fieldName, columnMeta.getColumnId());
                                sqlList.add(columnSql);
                            } else {
                                sqlList.add(alterSql + this.getAddColumnSql(columnMeta));
                                if (!DbTypeUtils.dbTypeIsSQLServer(dbType) && StringUtils.isNotEmpty(columnMeta.getComment())) {
                                    sqlList.add(this.getCommentSql(columnMeta));
                                }
                            }
                        } else {
                            ColumnMeta oldColumnMeta = (ColumnMeta) DbTableInfoMap.get(fieldName);
                            columnMeta = (ColumnMeta) columnMetaMap.get(fieldName);
                            if (!DbType.DB2.equals(dbType) && !DbType.HSQL.equals(dbType)) {
                                if (!oldColumnMeta.equalsByDbType(columnMeta, dbType)) {
                                    sqlList.add(alterSql + this.getUpdateColumnSql(columnMeta, oldColumnMeta));
                                }

                                if (!DbTypeUtils.dbTypeIsSQLServer(dbType) && !DbTypeUtils.dbTypeIsOracle(dbType) && !oldColumnMeta.isCommentEquals(columnMeta)) {
                                    sqlList.add(this.getCommentSql(columnMeta));
                                }
                            } else {
                                this.handleUpdateMultiSql(oldColumnMeta, columnMeta, tableName, sqlList);
                            }
                        }
                    }

                    iterator = DbTableInfoMap.keySet().iterator();

                    while (iterator.hasNext()) {
                        fieldName = (String) iterator.next();
                        if (!columnMetaMap.containsKey(fieldName.toLowerCase()) && !fieldNameMap.containsValue(fieldName.toLowerCase())) {
                            sqlList.add(alterSql + this.getDropColumnSql(fieldName));
                        }
                    }

                    if (DbType.DB2.equals(dbType)) {
                        sqlList.add("CALL SYSPROC.ADMIN_CMD('reorg table " + tableName + "')");
                    }

                    return sqlList;
                }
            }
        } catch (SQLException exp) {
            throw new RuntimeException();
        }
    }

    private Map<String, ColumnMeta> buildDbTableInfoMap(String tableName) throws SQLException {

        HashMap hashMap = new HashMap();
        Connection connection = null;

        try {
            connection = JdbcUtil.getConnection();
        } catch (Exception exp) {
            log.error(exp.getMessage(), exp);
        }

        DatabaseMetaData databaseMetaData = connection.getMetaData();
        String userName = databaseMetaData.getUserName();
        DbType dbType = JdbcUtil.getDatabaseTypeEnum();
        if (DbTypeUtils.dbTypeIsOracle(dbType) || DbType.DB2.equals(dbType)) {
            userName = userName.toUpperCase();
        }

        ResultSet resultSet = null;
        if (DbTypeUtils.dbTypeIsSQLServer(dbType)) {
            resultSet = databaseMetaData.getColumns(connection.getCatalog(), null, tableName, "%");
        } else if (DbTypeUtils.dbTypeIsPostgre(dbType)) {
            resultSet = databaseMetaData.getColumns(connection.getCatalog(), "public", tableName, "%");
        } else if (DbType.HSQL.equals(dbType)) {
            resultSet = databaseMetaData.getColumns(connection.getCatalog(), "PUBLIC", tableName.toUpperCase(), "%");
        } else {
            resultSet = databaseMetaData.getColumns(connection.getCatalog(), userName, tableName, "%");
        }

        while (resultSet.next()) {
            ColumnMeta columnMeta = new ColumnMeta();
            columnMeta.setTableName(tableName);
            String columnName = resultSet.getString("COLUMN_NAME").toLowerCase();
            columnMeta.setColumnName(columnName);
            String typeName = resultSet.getString("TYPE_NAME");
            int decimalDigits = resultSet.getInt("DECIMAL_DIGITS");
            String classType = dbTableHandleI.getMatchClassTypeByDataType(typeName, decimalDigits);
            columnMeta.setColunmType(classType);
            columnMeta.setRealDbType(typeName);
            int columnSize = resultSet.getInt("COLUMN_SIZE");
            columnMeta.setColumnSize(columnSize);
            columnMeta.setDecimalDigits(decimalDigits);
            String nullable = resultSet.getInt("NULLABLE") == 1 ? "Y" : "N";
            columnMeta.setIsNullable(nullable);
            String remarks = resultSet.getString("REMARKS");
            columnMeta.setComment(remarks);
            String columnDef = resultSet.getString("COLUMN_DEF");
            columnDef = trans(columnDef) == null ? "" : trans(columnDef);
            columnMeta.setFieldDefault(columnDef);
            hashMap.put(columnName, columnMeta);
        }

        return hashMap;
    }

    private Map<String, ColumnMeta> buildTableFieldsMap(TableInfo tableInfo) {
        HashMap hashMap = new HashMap();
        List columnList = tableInfo.getColumns();
        Iterator iterator = columnList.iterator();

        while (iterator.hasNext()) {
            OnlCgformField onlCgformField = (OnlCgformField) iterator.next();
            ColumnMeta columnMeta = new ColumnMeta();
            columnMeta.setTableName(tableInfo.getTableName().toLowerCase());
            columnMeta.setColumnId(onlCgformField.getId());
            columnMeta.setColumnName(onlCgformField.getDbFieldName().toLowerCase());
            columnMeta.setColumnSize(onlCgformField.getDbLength());
            columnMeta.setColunmType(onlCgformField.getDbType().toLowerCase());
            columnMeta.setIsNullable(onlCgformField.getDbIsNull() == 1 ? "Y" : "N");
            columnMeta.setComment(onlCgformField.getDbFieldTxt());
            columnMeta.setDecimalDigits(onlCgformField.getDbPointLength());
            columnMeta.setFieldDefault(trans(onlCgformField.getDbDefaultVal()));
            columnMeta.setPkType(tableInfo.getJformPkType() == null ? "UUID" : tableInfo.getJformPkType());
            columnMeta.setOldColumnName(onlCgformField.getDbFieldNameOld() != null ? onlCgformField.getDbFieldNameOld().toLowerCase() : null);
            hashMap.put(onlCgformField.getDbFieldName().toLowerCase(), columnMeta);
        }

        return hashMap;
    }

    private Map<String, String> buildDbFieldNameMap(List<OnlCgformField> onlCgformFieldList) {
        HashMap hashMap = new HashMap();
        Iterator iterator = onlCgformFieldList.iterator();

        while (iterator.hasNext()) {
            OnlCgformField onlCgformField = (OnlCgformField) iterator.next();
            hashMap.put(onlCgformField.getDbFieldName(), onlCgformField.getDbFieldNameOld());
        }

        return hashMap;
    }

    private void handleUpdateMultiSql(ColumnMeta oldColumnMeta, ColumnMeta newColumnMeta, String tableName, List<String> sqlList) {
        dbTableHandleI.handleUpdateMultiSql(oldColumnMeta, newColumnMeta, tableName, sqlList);
    }

    private String getUpdateColumnSql(ColumnMeta columnMeta, ColumnMeta oldColumnMeta) {
        return dbTableHandleI.getUpdateColumnSql(columnMeta, oldColumnMeta);
    }

    private String getSpecialHandle(ColumnMeta columnMeta, ColumnMeta oldColumnMeta) {
        return dbTableHandleI.getSpecialHandle(columnMeta, oldColumnMeta);
    }

    private String getCommentSql(ColumnMeta columnMeta) {
        return dbTableHandleI.getCommentSql(columnMeta);
    }

    private String getAddColumnSql(ColumnMeta columnMeta) {
        return dbTableHandleI.getAddColumnSql(columnMeta);
    }

    private String updateFiledNameOld(String fieldName, String columnId) {
        return "update onl_cgform_field set DB_FIELD_NAME_OLD = '" + fieldName + "' where ID ='" + columnId + "'";
    }

    public String buildCountIndexSql(String indexName, String tableName) {
        return dbTableHandleI.countIndex(indexName, tableName);
    }

    public String buildDropIndexSql(String indexName, String tableName) {
        return dbTableHandleI.dropIndexs(indexName, tableName);
    }

    private String getDropColumnSql(String fieldName) {
        return dbTableHandleI.getDropColumnSql(fieldName);
    }

    private static Map<String, Object> buildDataModel(TableInfo tableInfo, DbType dbType) {
        String dbTypeString = DbTypeUtils.getDbTypeString(dbType);
        HashMap hashMap = new HashMap();
        Iterator iterator = tableInfo.getColumns().iterator();

        while(iterator.hasNext()) {
            OnlCgformField onlCgformField = (OnlCgformField)iterator.next();
            onlCgformField.setDbDefaultVal(trans(onlCgformField.getDbDefaultVal()));
        }

        hashMap.put("entity", tableInfo);
        hashMap.put("dataType", dbTypeString);
        hashMap.put("db", dbType.getDb());
        return hashMap;
    }

}


