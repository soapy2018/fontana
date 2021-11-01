package com.bluetron.nb.common.db.query;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.core.toolkit.support.SerializedLambda;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author genx
 * @date 2021/5/27 9:45
 */
public class SqlUtils {

    private static Map<String, String> columnCache = new ConcurrentHashMap(256);


    public static String getTableAlias(Class entityType) {
        return "_" + StringUtils.firstToLowerCase(entityType.getSimpleName());
    }

    public static String getColumnName(Class entityType, String fieldName) {
        String key = entityType.getSimpleName() + "_" + fieldName;
        String columnName = columnCache.get(key);
        if (columnName == null) {
            TableInfo tableInfo = TableInfoHelper.getTableInfo(entityType);
            for (TableFieldInfo s : tableInfo.getFieldList()) {
                columnCache.putIfAbsent(entityType.getSimpleName() + "_" + s.getProperty(), s.getColumn());
                if (s.getProperty().equals(fieldName)) {
                    columnName = s.getColumn();
                }
            }
            columnCache.putIfAbsent(entityType.getSimpleName() + "_" + tableInfo.getKeyProperty(), tableInfo.getKeyColumn());
            if (tableInfo.getKeyProperty().equals(fieldName)) {
                columnName = tableInfo.getKeyColumn();
            }
        }
        if (columnName == null) {
            throw new RuntimeException("未知的列: " + entityType.getSimpleName() + " , " + fieldName);
        }
        return columnName;
    }

    public static String getSqlSegment(Class entityType) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(entityType);
        return tableInfo.getTableName() + " as " + getTableAlias(entityType);

    }

    public static String getSqlSegment(SFunction fun, Set<Class> entityTypeSet) {
        SerializedLambda serializedLambda = LambdaUtils.resolve(fun);
        if (entityTypeSet != null && !entityTypeSet.contains(serializedLambda.getInstantiatedType())) {
            throw new RuntimeException("无效的查询实体: " + serializedLambda.getInstantiatedType().getSimpleName());
        }
        return getTableAlias(serializedLambda.getInstantiatedType()) + "." + getColumnName(serializedLambda.getInstantiatedType(), PropertyNamer.methodToProperty(serializedLambda.getImplMethodName()));
    }

    public static String getPropertyName(SFunction fun) {
        SerializedLambda serializedLambda = LambdaUtils.resolve(fun);
        return PropertyNamer.methodToProperty(serializedLambda.getImplMethodName());
    }

    /**
     * 解析 select 选项
     * @param entityType
     * @param aliasMap
     * @param searchMapping
     * @param returnPropertySet
     */
    public static void resolveSelect(Class entityType, Map<String, String> aliasMap,  Map<String, String> searchMapping, Set<String> returnPropertySet, Set<String> ignoreSet) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(entityType);
        if (tableInfo == null) {
            throw new RuntimeException("不是有效的实体对象:" + entityType.getSimpleName());
        }
        String tableAlias = SqlUtils.getTableAlias(entityType);
        String name = aliasMap.get(tableInfo.getKeyProperty());
        if (name == null) {
            name = tableInfo.getKeyProperty();
        }
        if (returnPropertySet == null || returnPropertySet.contains(name)) {
            searchMapping.putIfAbsent(name, tableAlias + "." + tableInfo.getKeyColumn());
        }

        for (TableFieldInfo tableFieldInfo : tableInfo.getFieldList()) {
            if(ignoreSet != null && ignoreSet.contains(tableFieldInfo.getProperty())){
                //忽略
                continue;
            }
            name = aliasMap.get(tableFieldInfo.getProperty());
            if (name == null) {
                name = tableFieldInfo.getProperty();
            }
            if (returnPropertySet == null || returnPropertySet.contains(name)) {
                searchMapping.putIfAbsent(name, tableAlias + "." + tableFieldInfo.getColumn());
            }
        }
    }

}
