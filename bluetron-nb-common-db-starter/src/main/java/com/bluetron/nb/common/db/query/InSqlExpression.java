package com.bluetron.nb.common.db.query;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * Description: 
 * @author genx
 * @date 2021/6/17 14:27
 */
public class InSqlExpression extends AbsSqlExpression {

    private final String left;
    private final Collection data;

    public InSqlExpression(Set<Class> entityTypeSet, String left, Collection data) {
        super(entityTypeSet);
        this.left = left;
        this.data = parseParams(data);
    }

    @Override
    public void buildSql(StringBuilder sql, List<Object> params) {
        sql.append(this.left).append(" in ( ");
        int i = 0;
        for (Object datum : data) {
            if (i++ > 0) {
                sql.append(", ");
            }
            sql.append("?");
            params.add(datum);
        }
        sql.append(")");
    }
}
