package com.bluetron.nb.common.db.query;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author genx
 * @date 2021/6/16 21:51
 */
public class InSubQueryExpression<T, K> extends AbsSqlExpression {

    private final String leftSegment;

    private final Class<T> subEntityType;

    private final String selectSegment;

    /**
     * 条件
     */
    private final ListSqlExpression where;

    public InSubQueryExpression(String leftSegment, Class<T> subEntityType, SFunction<T, K> subFun, Consumer<ISqlExpression> consumer) {
        super(new HashSet(Arrays.asList(subEntityType)));
        this.leftSegment = leftSegment;
        this.subEntityType = subEntityType;
        this.selectSegment = SqlUtils.getSqlSegment(subFun, entityTypeSet);
        this.where = new ListSqlExpression(entityTypeSet);
        consumer.accept(this.where);
    }

    @Override
    public void buildSql(StringBuilder sql, List<Object> params) {
        sql.append(leftSegment);
        sql.append(" IN (");
        sql.append(" SELECT ");
        sql.append(selectSegment);
        sql.append(" FROM ");
        sql.append(SqlUtils.getSqlSegment(this.subEntityType));
        if (!where.isEmpty()) {
            sql.append(" WHERE ");
            this.where.buildSql(sql, params);
        }
        sql.append(")");
    }
}
