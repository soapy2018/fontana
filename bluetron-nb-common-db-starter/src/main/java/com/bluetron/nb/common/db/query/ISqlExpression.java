package com.bluetron.nb.common.db.query;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author genx
 * @date 2021/5/26 16:42
 */
public interface ISqlExpression {

    /**
     * 添加SQL段
     *
     * @param sqlExpression
     * @return
     */
    ISqlExpression append(ISqlExpression sqlExpression);

    /**
     * 构建SQL语句
     *
     * @param sql
     * @param params
     * @return
     */
    void buildSql(StringBuilder sql, List<Object> params);

    /**
     * 联表时 两张表的关系
     *
     * @param s1
     * @param s2
     * @param <E1>
     * @param <E2>
     * @param <T>
     * @return
     */
    <E1, E2, T> ISqlExpression on(SFunction<E1, T> s1, SFunction<E2, T> s2);

    /**
     * 等于
     *
     * @return
     */
    <E, T> ISqlExpression eq(boolean condition, SFunction<E, T> fun, T data);

    default <E, T> ISqlExpression eq(SFunction<E, T> s1, T data) {
        return this.eq(true, s1, data);
    }

    default <E> ISqlExpression like(SFunction<E, String> fun, String data) {
        return this.like(true, fun, data);
    }

    <E> ISqlExpression like(boolean condition, SFunction<E, String> fun, String data);

    default <E, T> ISqlExpression ge(SFunction<E, T> fun, T data) {
        return this.ge(true, fun, data);
    }

    /**
     * >=
     */
    <E, T> ISqlExpression ge(boolean condition, SFunction<E, T> fun, T data);

    default <E, T> ISqlExpression le(SFunction<E, T> fun, T data) {
        return this.le(true, fun, data);
    }

    /**
     * <=
     */
    <E, T> ISqlExpression le(boolean condition, SFunction<E, T> fun, T data);

    <E, T> ISqlExpression lt(boolean condition, SFunction<E, T> fun, T data);

    <E1, E2, T> ISqlExpression onLt(SFunction<E1, T> s1, SFunction<E2, T> s2);

    ISqlExpression addCondition(boolean condition, NormalSqlExpression.Type type, String leftSegment, Object data);

    <E, T> ISqlExpression ne(boolean condition, SFunction<E, T> fun, T data);

    default <E, T> ISqlExpression ne(SFunction<E, T> s1, T data) {
        return this.ne(true, s1, data);
    }

    <E, T> ISqlExpression in(boolean condition, SFunction<E, T> fun, Collection<T> data);

    default <E, T> ISqlExpression in(SFunction<E, T> fun, Collection<T> data) {
        return this.in(true, fun, data);
    }


    <T> ISqlExpression in(boolean condition, String leftSqlSegment, Collection<T> data);

    ISqlExpression or(boolean condition, Consumer<ISqlExpression> consumer);

    default ISqlExpression or(Consumer<ISqlExpression> consumer) {
        return this.or(true, consumer);
    }

    /**
     * in 子查询
     *
     * @param condition
     * @param fun
     * @param subEntityType
     * @param subFun
     * @param consumer
     * @param <E>
     * @param <T>
     * @param <E2>
     * @return
     */
    <E, T, E2> ISqlExpression inSubQuery(boolean condition, SFunction<E, T> fun, Class<E2> subEntityType, SFunction<E2, T> subFun, Consumer<ISqlExpression> consumer);

    default <E, T, E2> ISqlExpression inSubQuery(SFunction<E, T> fun, Class<E2> subEntityType, SFunction<E2, T> subFun, Consumer<ISqlExpression> consumer) {
        return this.inSubQuery(true, fun, subEntityType, subFun, consumer);
    }

}
