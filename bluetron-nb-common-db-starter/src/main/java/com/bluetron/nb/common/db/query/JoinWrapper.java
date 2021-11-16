package com.bluetron.nb.common.db.query;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.util.*;
import java.util.function.Consumer;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author genx
 * @date 2021/5/27 14:33
 */
public class JoinWrapper<E, J, R> {
    private final JoinType joinType;
    private final Class<J> joinEntityType;
    private final Consumer<ISqlExpression> on;
    private final Set<Class> entityTypeSet;
    private Map<String, String> aliasMap = new HashMap(16);
    /**
     * 需要忽略的字段
     */
    private Set<String> ignoreSet = new HashSet(16);
    public JoinWrapper(JoinType joinType, Class<J> joinEntityType, Consumer<ISqlExpression> on, Set<Class> entityTypeSet) {
        this.joinType = joinType;
        this.joinEntityType = joinEntityType;
        this.on = on;
        this.entityTypeSet = entityTypeSet;
    }

    public <T> JoinWrapper<E, J, R> alias(SFunction<J, T> f1, SFunction<R, T> f2) {
        aliasMap.put(SqlUtils.getPropertyName(f1), SqlUtils.getPropertyName(f2));
        return this;
    }

    /**
     * 忽略一些字段
     *
     * @param ff
     * @return
     */
    public JoinWrapper<E, J, R> ignore(SFunction<J, ?>... ff) {
        if (ff != null && ff.length > 0) {
            for (SFunction<J, ?> f : ff) {
                if (f != null) {
                    //用空字符串来代表 忽略
                    ignoreSet.add(SqlUtils.getPropertyName(f));
                }
            }
        }
        return this;
    }

    public Map<String, String> getAliasMap() {
        return aliasMap;
    }

    public JoinType getJoinType() {
        return joinType;
    }

    public Class<J> getJoinEntityType() {
        return joinEntityType;
    }

    /**
     * 构建SQL语句
     *
     * @param sql
     * @param params
     * @return
     */
    public void buildSql(StringBuilder sql, List<Object> params) {
        sql.append(" ").append(this.joinType.name()).append(" JOIN ");
        sql.append(SqlUtils.getSqlSegment(this.joinEntityType));
        ListSqlExpression sqlExpression = new ListSqlExpression(this.entityTypeSet);
        on.accept(sqlExpression);
        if (sqlExpression.isEmpty()) {
            throw new RuntimeException("联表查询缺少条件:" + this.joinEntityType.getSimpleName());
        }
        sql.append(" ON ");
        sqlExpression.buildSql(sql, params);
    }

    public Set<String> getIgnoreSet() {
        return ignoreSet;
    }

    public enum JoinType {
        LEFT,
        RIGHT,
        INNER
    }
}
