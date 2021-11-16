package com.bluetron.nb.common.db.query;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 *
 * @author genx
 * @date 2021/5/27 18:07
 */
public class SearchHandler {

    private final NormalSqlExpression.Type type;
    private final Class paramType;

    private SearchHandler(NormalSqlExpression.Type type, Class paramType) {
        this.type = type;
        this.paramType = paramType;
    }

    public static SearchHandler create(NormalSqlExpression.Type type, Class paramType) {
        return new SearchHandler(type, paramType);
    }

    public void addCondition(ISqlExpression sqlExpression, String leftSegment, Object data) {
        if (data == null) {
            return;
        }
        if (data instanceof Iterable || data.getClass().isArray()) {
            final List<Object> params = new LinkedList();
            if (data instanceof Iterable) {
                for (Object datum : (Iterable) data) {
                    params.add(ConverterUtil.convert(datum, this.paramType));
                }
            } else {
                for (Object datum : (Object[]) data) {
                    params.add(ConverterUtil.convert(datum, this.paramType));
                }
            }
            if (this.type == NormalSqlExpression.Type.EQ) {
                //in查询
                sqlExpression.in(true, leftSegment, params);
            } else {
                //or查询
                sqlExpression.or(c -> {
                    for (Object param : params) {
                        c.addCondition(true, this.type, leftSegment, param);
                    }
                });
            }
        } else {
            if (data instanceof CharSequence && "".equals(data.toString())) {
                //忽略 空字符串
                return;
            }
            sqlExpression.addCondition(true, this.type, leftSegment, ConverterUtil.convert(data, this.paramType));
        }
    }
}
