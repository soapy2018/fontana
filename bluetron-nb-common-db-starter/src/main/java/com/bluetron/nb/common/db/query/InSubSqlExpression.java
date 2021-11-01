package com.bluetron.nb.common.db.query;

import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * Description: 
 * @author genx
 * @date 2021/6/16 9:11
 */
public class InSubSqlExpression extends AbsSqlExpression {

    private final String leftSegment;

    private final String sqlSegment;

    private final Object[] paramArray;

    public InSubSqlExpression(Set<Class> entityTypeSet, String leftSegment, String sqlSegment, Object... paramArray) {
        super(entityTypeSet);
        this.leftSegment = leftSegment;
        this.sqlSegment = sqlSegment;
        this.paramArray = parseParams(paramArray);
    }


    @Override
    public void buildSql(StringBuilder sql, List<Object> params) {
        sql.append(leftSegment);
        sql.append(" in (");
        sql.append(sqlSegment);
        sql.append(")");
        if (paramArray != null && paramArray.length > 0) {
            for (Object param : paramArray) {
                params.add(param);
            }
        }
    }
}
