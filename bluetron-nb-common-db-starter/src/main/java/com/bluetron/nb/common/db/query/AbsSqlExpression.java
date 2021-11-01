package com.bluetron.nb.common.db.query;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author genx
 * @date 2021/5/26 16:55
 */
public abstract class AbsSqlExpression implements ISqlExpression {

    protected final Set<Class> entityTypeSet;

    public AbsSqlExpression(Set<Class> entityTypeSet) {
        this.entityTypeSet = entityTypeSet;
    }

    @Override
    public ISqlExpression append(ISqlExpression sqlExpression) {
        ListSqlExpression list;
        if (this instanceof ListSqlExpression) {
            list = (ListSqlExpression) this;
        } else {
            list = new ListSqlExpression(entityTypeSet);
            list.append(this);
        }
        list.append(sqlExpression);
        return list;
    }

    @Override
    public <E1, E2, T> ISqlExpression on(SFunction<E1, T> s1, SFunction<E2, T> s2) {
        return this.append(
                NormalSqlExpression.createByBilateral(
                        this.entityTypeSet,
                        NormalSqlExpression.Type.EQ,
                        SqlUtils.getSqlSegment(s1, this.entityTypeSet), SqlUtils.getSqlSegment(s2, this.entityTypeSet))
        );
    }

    @Override
    public <E, T> ISqlExpression eq(boolean condition, SFunction<E, T> fun, T data) {
        return this.addCondition(condition, NormalSqlExpression.Type.EQ, SqlUtils.getSqlSegment(fun, this.entityTypeSet), data);
    }

    @Override
    public <E> ISqlExpression like(boolean condition, SFunction<E, String> fun, String data) {
        if (StringUtils.isNotBlank(data)) {
            this.addCondition(condition, NormalSqlExpression.Type.LIKE, SqlUtils.getSqlSegment(fun, this.entityTypeSet), data);
        }
        return this;
    }

    @Override
    public <E, T> ISqlExpression ge(boolean condition, SFunction<E, T> fun, T data) {
        return this.addCondition(condition, NormalSqlExpression.Type.GE, SqlUtils.getSqlSegment(fun, this.entityTypeSet), data);
    }

    @Override
    public <E, T> ISqlExpression le(boolean condition, SFunction<E, T> fun, T data) {
        return this.addCondition(condition, NormalSqlExpression.Type.LE, SqlUtils.getSqlSegment(fun, this.entityTypeSet), data);
    }

    @Override
    public <E, T> ISqlExpression lt(boolean condition, SFunction<E, T> fun, T data) {
        return this.addCondition(condition, NormalSqlExpression.Type.LT, SqlUtils.getSqlSegment(fun, this.entityTypeSet), data);
    }

    @Override
    public <E1, E2, T> ISqlExpression onLt(SFunction<E1, T> s1, SFunction<E2, T> s2) {
        return this.append(
                NormalSqlExpression.createByBilateral(
                        this.entityTypeSet,
                        NormalSqlExpression.Type.LT,
                        SqlUtils.getSqlSegment(s1, this.entityTypeSet), SqlUtils.getSqlSegment(s2, this.entityTypeSet))
        );
    }

    @Override
    public ISqlExpression addCondition(boolean condition, NormalSqlExpression.Type type, String leftSegment, Object data) {
        if (!condition) {
            return this;
        }
        return this.append(
                NormalSqlExpression.createByData(
                        this.entityTypeSet,
                        type,
                        leftSegment,
                        data)
        );
    }

    @Override
    public <E, T> ISqlExpression ne(boolean condition, SFunction<E, T> fun, T data) {
        if (!condition) {
            return this;
        }
        return this.append(
                NormalSqlExpression.createByData(
                        this.entityTypeSet,
                        NormalSqlExpression.Type.NE,
                        SqlUtils.getSqlSegment(fun, this.entityTypeSet), data));
    }

    @Override
    public <E, T> ISqlExpression in(boolean condition, SFunction<E, T> fun, Collection<T> data) {
        this.in(condition, SqlUtils.getSqlSegment(fun, this.entityTypeSet), data);
        return this;
    }

    @Override
    public <T> ISqlExpression in(boolean condition, String leftSqlSegment, Collection<T> data) {
        if (!condition) {
            return this;
        }
        if (data == null || data.size() == 0) {
            return this;
        }
        this.append(new InSqlExpression(this.entityTypeSet, leftSqlSegment, data));
        return this;
    }


    @Override
    public ISqlExpression or(boolean condition, Consumer<ISqlExpression> consumer) {
        if (!condition) {
            return this;
        }
        ISqlExpression next = new ListSqlExpression(SqlConnectSegment.OR, this.entityTypeSet);
        this.append(next);
        consumer.accept(next);
        return this;
    }

    @Deprecated
    public <E, T> ISqlExpression inSql(boolean condition, SFunction<E, T> fun, String sql, Object... params) {
        if (!condition) {
            return this;
        }
        return this.append(new InSubSqlExpression(this.entityTypeSet, SqlUtils.getSqlSegment(fun, this.entityTypeSet), sql, params));
    }

    @Override
    public <E, T, E2> ISqlExpression inSubQuery(boolean condition, SFunction<E, T> fun, Class<E2> subEntityType, SFunction<E2, T> subFun, Consumer<ISqlExpression> consumer) {
        if (!condition) {
            return this;
        }

        this.append(new InSubQueryExpression(SqlUtils.getSqlSegment(fun, this.entityTypeSet), subEntityType, subFun, consumer));
        return this;
    }

    protected Collection parseParams(Collection param) {
        if (param == null) {
            return null;
        }
        List result = new ArrayList(param.size());
        for (Object o : param) {
            result.add(parseParam(o));
        }
        return result;
    }

    protected Object[] parseParams(Object[] param) {
        if (param == null) {
            return null;
        }
        for (int i = 0; i < param.length; i++) {
            param[i] = parseParam(param[i]);
        }
        return param;
    }

    protected Object parseParam(Object param) {
        if (param == null) {
            return null;
        }
        if (param instanceof Enum) {
            param = ((Enum) param).name();
        }
        return param;
    }
}
