package com.fontana.db.util;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.toolkit.JdbcUtils;
import com.fontana.base.exception.GeneralException;
import com.fontana.db.service.IDbTableHandle;
import com.fontana.db.service.impl.MySqlDbTableHandle;
import com.fontana.db.service.impl.OracleDbTableHandle;
import com.fontana.util.tools.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @className: JdbcUtil
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/1/11 16:51
 */
@Slf4j
public class JdbcUtil {

    /** 当前系统数据库类型 */
    private static DbType dbTypeEnum = null;
    /**
     * 全局获取平台数据库类型（对应mybaisPlus枚举）
     *
     * @return
     */
    public static DbType getDatabaseTypeEnum() {

        if (ObjectUtil.isNotEmpty(dbTypeEnum)) {
            return dbTypeEnum;
        }

        try {
            DataSource dataSource = SpringContextHolder.getApplicationContext().getBean(DataSource.class);
            dbTypeEnum =  JdbcUtils.getDbType(dataSource.getConnection().getMetaData().getURL());
            return dbTypeEnum;

        } catch (SQLException e) {
            log.warn(e.getMessage(), e);
            return null;
        }
    }

    public static Connection getConnection() throws SQLException {
        try(Connection connection= SpringContextHolder.getApplicationContext().getBean(DataSource.class).getConnection()){
            return connection;
        }catch (SQLException exp) {
            throw new GeneralException(exp.getMessage());
        }
    }

    public static DatabaseMetaData getMetaData() throws SQLException {
        return getConnection().getMetaData();
    }

    public static String getUserName() throws SQLException {
        return getMetaData().getUserName();
    }

//    public static String getPassword() throws SQLException {
//        //return getMetaData().ge;
//    }

    public static String getDriverClassName() throws SQLException {
        return getMetaData().getDriverName();
    }

    public static String getUrl() throws SQLException {
        return getMetaData().getURL();
    }

    public static Boolean isTableExist(String tableName) {
        return isTableExist(tableName, null);
    }

    public static Boolean isTableExist(String tableName, DbType dbType) {

        Connection connection = null;
        ResultSet resultSet = null;
        try {
            String[] types = new String[]{"TABLE"};
            connection = JdbcUtil.getConnection();
            DatabaseMetaData databaseMetaData = connection.getMetaData();

            if(null == dbType) {
                dbType = JdbcUtils.getDbType(databaseMetaData.getURL());
            }

            String dbTypeString = DbTypeUtils.getDbTypeString(dbType);

            tableName = ConvertTableNameByDbType(tableName, dbTypeString);

            String dbUserName = databaseMetaData.getUserName();
            if (DbTypeUtils.dbTypeIsOracle(dbType) || DbType.DB2.equals(dbType)) {
                dbUserName = dbUserName != null ? dbUserName.toUpperCase() : null;
            }

            if (DbTypeUtils.dbTypeIsSQLServer(dbType)) {
                resultSet = databaseMetaData.getTables(connection.getCatalog(), null, tableName, types);
            } else if (DbTypeUtils.dbTypeIsPostgre(dbType)) {
                resultSet = databaseMetaData.getTables(connection.getCatalog(), "public", tableName, types);
            } else if (DbType.HSQL.equals(dbType)) {
                resultSet = databaseMetaData.getTables(connection.getCatalog(), "PUBLIC", tableName.toUpperCase(), types);
            } else {
                resultSet = databaseMetaData.getTables(connection.getCatalog(), dbUserName, tableName, types);
            }
            return resultSet.next();

        } catch (SQLException exp) {
            throw new RuntimeException();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }

                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException exp) {
                log.error(exp.getMessage(), exp);
            }
        }
    }

    public static String ConvertTableNameByDbType(String tableName, String dbType) {
        byte type = -1;
        switch (dbType.hashCode()) {
            case -1955532418:
                if (dbType.equals("ORACLE")) {
                    type = 0;
                }
                break;
            case -1620389036:
                if (dbType.equals("POSTGRESQL")) {
                    type = 2;
                }
                break;
            case 67444:
                if (dbType.equals("DB2")) {
                    type = 1;
                }
        }

        switch (type) {
            case 0:
            case 1:
                return tableName.toUpperCase();
            case 2:
                return tableName.toLowerCase();
            default:
                return tableName;
        }
    }

    public static IDbTableHandle getDbTableHandle(DbType dbType) {

        String dbTypeString = DbTypeUtils.getDbTypeString(dbType);
        byte type = -1;
        switch(dbTypeString.hashCode()) {
            case -1955532418:
                if (dbTypeString.equals("ORACLE")) {
                    type = 2;
                }
                break;
            case -1620389036:
                if (dbTypeString.equals("POSTGRESQL")) {
                    type = 5;
                }
                break;
            case 2185:
                if (dbTypeString.equals("DM")) {
                    type = 3;
                }
                break;
            case 67444:
                if (dbTypeString.equals("DB2")) {
                    type = 6;
                }
                break;
            case 2227302:
                if (dbTypeString.equals("HSQL")) {
                    type = 7;
                }
                break;
            case 73844866:
                if (dbTypeString.equals("MYSQL")) {
                    type = 0;
                }
                break;
            case 912124529:
                if (dbTypeString.equals("SQLSERVER")) {
                    type = 4;
                }
                break;
            case 1557169620:
                if (dbTypeString.equals("MARIADB")) {
                    type = 1;
                }
        }

        Object dbTableHandle = null;

        switch(type) {
            case 0:
                dbTableHandle = new MySqlDbTableHandle();
                break;
            case 1:
                dbTableHandle = new MySqlDbTableHandle();
                break;
            case 2:
                dbTableHandle = new OracleDbTableHandle();
                break;
            case 3:
                //dbTableHandle = new DMDbTableHandle();  //达梦
                break;
            case 4:
                //dbTableHandle = new SqlServerDbTableHandle(); // SqlServer
                break;
            case 5:
                //dbTableHandle = new PostgreSqlDbTableHandle(); //PostgreSql
                break;
            case 6:
                //dbTableHandle = new DB2DbTableHandle(); // DB2
                break;
            case 7:
                //dbTableHandle = new HSqlDbTableHandle();
                break;
            default:
                dbTableHandle = new MySqlDbTableHandle(); //MySql
        }

        return (IDbTableHandle)dbTableHandle;
    }

}


