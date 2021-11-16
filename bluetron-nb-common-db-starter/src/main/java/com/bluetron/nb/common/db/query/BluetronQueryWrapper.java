package com.bluetron.nb.common.db.query;


import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.function.Consumer;

/**
 * 多表联查
 *
 * @author genx
 * @date 2021/5/27 10:52
 */
public class BluetronQueryWrapper<E, R> {

    /**
     * 主表实体对象
     */
    private final Class<E> entityType;

    /**
     * 返回的对象
     */
    private final Class<R> returnType;

    /**
     * 连接表信息
     */
    private final LinkedHashMap<Class, JoinWrapper> joinMap = new LinkedHashMap();

    /**
     * 当前已包含的实体对象
     */
    private final Set<Class> entityTypeSet = new HashSet(8);
    /**
     * 条件
     */
    private final ISqlExpression where;
    /**
     * 排序
     */
    private final List<String> orderByList = new LinkedList();
    /**
     * 主表的别名
     * (连接表的别名在 JoinWrapper 维护)
     */
    private Map<String, String> aliasMap = new HashMap(16);
    /**
     * 需要忽略的字段
     */
    private Set<String> ignoreSet = new HashSet(16);
    /**
     * 查询的key 与 数据库真实字段的映射关系
     * {
     * "workOrderNo": "_workOrder.code"
     * }
     */
    private Map<String, String> searchMapping = new HashMap(64);

    public BluetronQueryWrapper(Class<E> entityType, Class<R> returnType) {
        this.entityType = entityType;
        this.returnType = returnType;
        this.entityTypeSet.add(entityType);
        this.where = new ListSqlExpression(entityTypeSet);
    }

    /**
     * 设置别名
     *
     * @param f1
     * @param f2
     * @param <T>
     * @return
     */
    public <T> BluetronQueryWrapper<E, R> alias(SFunction<E, T> f1, SFunction<R, T> f2) {
        aliasMap.put(SqlUtils.getPropertyName(f1), SqlUtils.getPropertyName(f2));
        return this;
    }

    /**
     * 忽略一些字段
     *
     * @param ff
     * @return
     */
    public BluetronQueryWrapper<E, R> ignore(SFunction<E, ?>... ff) {
        if (ff != null && ff.length > 0) {
            for (SFunction<E, ?> f : ff) {
                if (f != null) {
                    //用空字符串来代表 忽略
                    ignoreSet.add(SqlUtils.getPropertyName(f));
                }
            }
        }
        return this;
    }

    public <J> JoinWrapper<E, J, R> leftJoin(Class<J> joinEntityType, Consumer<ISqlExpression> consumer) {
        return this.join(JoinWrapper.JoinType.LEFT, joinEntityType, consumer);
    }

    public <J> JoinWrapper<E, J, R> innerJoin(Class<J> joinEntityType, Consumer<ISqlExpression> consumer) {
        return this.join(JoinWrapper.JoinType.INNER, joinEntityType, consumer);
    }

    public <J> JoinWrapper<E, J, R> join(JoinWrapper.JoinType joinType, Class<J> joinEntityType, Consumer<ISqlExpression> consumer) {
        if (joinMap.containsKey(joinEntityType)) {
            throw new RuntimeException("暂不支持相同表的连接");
        }
        JoinWrapper joinWrapper = new JoinWrapper(joinType, joinEntityType, consumer, entityTypeSet);
        joinMap.putIfAbsent(joinEntityType, joinWrapper);
        entityTypeSet.add(joinEntityType);
        return joinWrapper;
    }


    public ISqlExpression where() {
        return where;
    }

    public <EE, T> BluetronQueryWrapper<E, R> orderByAsc(SFunction<EE, T> s1) {
        return this.orderBy(s1, true);
    }

    public <EE, T> BluetronQueryWrapper<E, R> orderByDesc(SFunction<EE, T> s1) {
        return this.orderBy(s1, false);
    }

    public BluetronQueryWrapper<E, R> orderBy(String sqlSegment, boolean isAsc) {
        orderByList.add(sqlSegment + (isAsc ? "" : " desc"));
        return this;
    }

    public <EE, T> BluetronQueryWrapper<E, R> orderBy(SFunction<EE, T> s1, boolean isAsc) {
        orderByList.add(SqlUtils.getSqlSegment(s1, this.entityTypeSet) + (isAsc ? "" : " desc"));
        return this;
    }

    private void buildSelect() {
        Set<String> returnPropertySet = null;
        if (!Map.class.isAssignableFrom(this.returnType)) {
            BeanWrapper beanWrapper = new BeanWrapperImpl(this.returnType);
            returnPropertySet = new HashSet(32);
            for (PropertyDescriptor propertyDescriptor : beanWrapper.getPropertyDescriptors()) {
                returnPropertySet.add(propertyDescriptor.getName());
            }
        }
        SqlUtils.resolveSelect(this.entityType, this.aliasMap, this.searchMapping, returnPropertySet, this.ignoreSet);
        for (JoinWrapper joinWrapper : joinMap.values()) {
            SqlUtils.resolveSelect(joinWrapper.getJoinEntityType(), joinWrapper.getAliasMap(), this.searchMapping, returnPropertySet, joinWrapper.getIgnoreSet());
        }
    }

    public String getHeader() {
        StringBuilder sql = new StringBuilder(256);
        buildSelect();
        sql.append("SELECT ");
        if (this.searchMapping.size() == 0) {
            sql.append("*");
        } else {
            int i = 0;
            for (Map.Entry<String, String> item : this.searchMapping.entrySet()) {
                if (i++ > 0) {
                    sql.append(", ");
                }
                sql.append(item.getValue()).append(" as ").append(item.getKey());
            }
        }
        return sql.toString();
    }

    public void buildWhereSql(StringBuilder sql, List<Object> params) {

        sql.append(" FROM ");

        sql.append(SqlUtils.getSqlSegment(this.entityType));

        if (joinMap.size() > 0) {
            for (JoinWrapper joinWrapper : joinMap.values()) {
                joinWrapper.buildSql(sql, params);
            }
        }
        sql.append(" WHERE ");
        this.where.buildSql(sql, params);


    }

    public String getOrderBySql() {
        StringBuilder sql = new StringBuilder(64);
        if (this.orderByList.size() > 0) {
            sql.append(" ORDER BY ");
            sql.append(StringUtils.join(this.orderByList, ", "));
        }
        return sql.toString();
    }

    public Map<String, String> getSearchMapping() {
        if (searchMapping.size() == 0) {
            buildSelect();
        }
        return searchMapping;
    }

    public Class<E> getEntityType() {
        return entityType;
    }

    public Class<R> getReturnType() {
        return returnType;
    }
}
