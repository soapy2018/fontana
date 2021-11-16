package com.bluetron.nb.common.db.query;

import com.bluetron.nb.common.util.tools.SpecialCharUtil;

import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author genx
 * @date 2021/5/26 16:43
 */
public class NormalSqlExpression extends AbsSqlExpression {

    private Type type;
    private String left;
    private String right;
    private Object data;
    private NormalSqlExpression(Set<Class> entityTypeSet, Type type, String left, String right, Object data) {
        super(entityTypeSet);
        this.type = type;
        this.left = left;
        this.right = right;
        this.data = parseParam(data);
    }

    /**
     * 没有值得情况 and table1.name = table2.name
     *
     * @param type
     * @param left
     * @param right
     * @return
     */
    public static NormalSqlExpression createByBilateral(Set<Class> entityTypeSet, Type type, String left, String right) {
        return new NormalSqlExpression(entityTypeSet, type, left, right, null);
    }

    public static NormalSqlExpression createByData(Set<Class> entityTypeSet, Type type, String left, Object data) {
        if (data != null && data instanceof Enum) {
            //枚举类特殊处理
            data = ((Enum) data).name();
        }
        return new NormalSqlExpression(entityTypeSet, type, left, null, data);
    }

    @Override
    public void buildSql(StringBuilder sql, List<Object> params) {
        sql.append(this.left).append(" ").append(this.type.symbol).append(" ");
        if (this.right != null) {
            sql.append(this.right);
        } else {
            sql.append("?");
            if (type == Type.LIKE) {
                params.add(SpecialCharUtil.parseLikeSpecialChar(String.valueOf(this.data)));
            } else {
                params.add(this.data);
            }

        }
    }


    public enum Type {
        LIKE("LIKE"),
        NOT_LIKE("NOT LIKE"),
        EQ("="),
        NE("<>"),
        GT(">"),
        GE(">="),
        LT("<"),
        LE("<=");

        private String symbol;

        Type(String symbol) {
            this.symbol = symbol;
        }
    }


}
