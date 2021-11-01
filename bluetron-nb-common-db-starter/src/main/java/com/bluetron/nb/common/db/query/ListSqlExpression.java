package com.bluetron.nb.common.db.query;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author genx
 * @date 2021/5/26 16:43
 */
public class ListSqlExpression extends AbsSqlExpression {

    private List<ISqlExpression> sqlExpressionList = new LinkedList();

    private final SqlConnectSegment sqlConnectSegment;


    public ListSqlExpression(Set<Class> entityTypeSet) {
        this(SqlConnectSegment.AND, entityTypeSet);
    }

    public ListSqlExpression(SqlConnectSegment sqlConnectSegment, Set<Class> entityTypeSet) {
        super(entityTypeSet);
        this.sqlConnectSegment = sqlConnectSegment != null ? sqlConnectSegment : SqlConnectSegment.AND;
    }

    @Override
    public ListSqlExpression append(ISqlExpression sqlExpression) {
        sqlExpressionList.add(sqlExpression);
        return this;
    }

    @Override
    public void buildSql(StringBuilder sql, List<Object> params) {
        if (this.sqlExpressionList.size() == 0) {
            //空的话 使用 1=1 填充
            sql.append("1 = 1");
            return;
        }
        int i = 0;
        if (this.sqlExpressionList.size() > 1 && sqlConnectSegment == SqlConnectSegment.OR) {
            //OR 模式才需要加括号
            sql.append("(");
        }
        for (ISqlExpression sqlExpression : sqlExpressionList) {
            if (i > 0) {
                sql.append(" ").append(sqlConnectSegment.name()).append(" ");
            }
            sqlExpression.buildSql(sql, params);
            i++;
        }
        if (this.sqlExpressionList.size() > 1 && sqlConnectSegment == SqlConnectSegment.OR) {
            sql.append(") ");
        }
    }

    public boolean isEmpty() {
        return this.sqlExpressionList.size() == 0;
    }

}
