package com.bluetron.nb.common.db.query;

import cn.hutool.core.util.ReflectUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 给QueryWrapper追加条件
 *
 * @author genx
 * @date 2021/6/15 20:54
 */
public class ConditionUtils {

    public static QueryCondition readQueryCondition(Map<String, String[]> parameterMap) {
        QueryCondition queryCondition = new QueryCondition();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            if (entry.getValue() == null || entry.getValue().length == 0) {
                continue;
            }
            if (entry.getValue().length == 1) {
                queryCondition.put(entry.getKey(), entry.getValue()[0]);
            } else {
                queryCondition.put(entry.getKey(), entry.getValue());
            }
        }
        return queryCondition;
    }

    public static <E, T> void appendCondition(BluetronQueryWrapper<E, T> queryWrapper, QueryCondition<T> queryCondition) {
        Map<String, Object> condition = new HashMap(32);
        for (Map.Entry<String, Object> entry : queryCondition.entrySet()) {
            condition.put(entry.getKey(), entry.getValue());
        }
        appendCondition(queryWrapper, condition, queryCondition.getOrderBy(), queryCondition.isAsc());
    }

    public static <E, T> void appendCondition(BluetronQueryWrapper<E, T> queryWrapper, IQueryCondition<T> queryCondition) {
        Map<String, Object> condition = new HashMap(32);
        for (Map.Entry<String, Object> entry : queryCondition.entrySet()) {
            condition.put(entry.getKey(), entry.getValue());
        }
        appendCondition(queryWrapper, condition, queryCondition.getOrderBy(), queryCondition.isAsc());
    }

    public static <E, T> void appendCondition(BluetronQueryWrapper<E, T> queryWrapper, Map<String, Object> condition) {
        appendCondition(queryWrapper, condition, null, true);
    }

    /**
     * 追加条件
     *
     * @param queryWrapper
     * @param condition
     * @param <E>
     * @param <T>
     */
    public static <E, T> void appendCondition(BluetronQueryWrapper<E, T> queryWrapper, Map<String, Object> condition, String orderBy, boolean isAsc) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(queryWrapper.getReturnType());
        Map<String, String> propertyMap = new HashMap(16);
        Map<String, SearchHandler> handlerMap = new HashMap(64);

        for (PropertyDescriptor propertyDescriptor : beanWrapper.getPropertyDescriptors()) {
            if (!queryWrapper.getSearchMapping().containsKey(propertyDescriptor.getName())) {
                continue;
            }
            handlerMap.put(propertyDescriptor.getName() + "Equal", SearchHandler.create(NormalSqlExpression.Type.EQ, propertyDescriptor.getPropertyType()));
            propertyMap.put(propertyDescriptor.getName() + "Equal", propertyDescriptor.getName());

            if (CharSequence.class.isAssignableFrom(propertyDescriptor.getPropertyType()) && !isQueryByEqual(queryWrapper.getReturnType(), propertyDescriptor.getName())) {
                //!!当前默认时like 查询
                handlerMap.put(propertyDescriptor.getName(), SearchHandler.create(NormalSqlExpression.Type.LIKE, propertyDescriptor.getPropertyType()));
                propertyMap.put(propertyDescriptor.getName(), propertyDescriptor.getName());
                //字符型 可以用 Like
                handlerMap.put(propertyDescriptor.getName() + "Like", SearchHandler.create(NormalSqlExpression.Type.LIKE, propertyDescriptor.getPropertyType()));
                propertyMap.put(propertyDescriptor.getName() + "Like", propertyDescriptor.getName());
            } else {
                handlerMap.put(propertyDescriptor.getName(), SearchHandler.create(NormalSqlExpression.Type.EQ, propertyDescriptor.getPropertyType()));
                propertyMap.put(propertyDescriptor.getName(), propertyDescriptor.getName());
            }
            if (Date.class.isAssignableFrom(propertyDescriptor.getPropertyType())
                    || Number.class.isAssignableFrom(propertyDescriptor.getPropertyType())) {
                //日期或数值型可以用 >=  <=
                handlerMap.put(propertyDescriptor.getName() + "Start", SearchHandler.create(NormalSqlExpression.Type.GE, propertyDescriptor.getPropertyType()));
                propertyMap.put(propertyDescriptor.getName() + "Start", propertyDescriptor.getName());
                handlerMap.put(propertyDescriptor.getName() + "End", SearchHandler.create(NormalSqlExpression.Type.LE, propertyDescriptor.getPropertyType()));
                propertyMap.put(propertyDescriptor.getName() + "End", propertyDescriptor.getName());
            }
        }

        SearchHandler searchHandler;
        String leftSegment;
        for (Map.Entry<String, Object> entry : condition.entrySet()) {
            searchHandler = handlerMap.get(entry.getKey());
            leftSegment = queryWrapper.getSearchMapping().get(propertyMap.get(entry.getKey()));
            if (searchHandler != null && leftSegment != null) {
                searchHandler.addCondition(queryWrapper.where(), leftSegment, entry.getValue());
            }
        }


        if (StringUtils.isNotBlank(orderBy)) {
            leftSegment = queryWrapper.getSearchMapping().get(propertyMap.get(orderBy));
            if (leftSegment != null) {
                queryWrapper.orderBy(leftSegment, isAsc);
            }
        }
    }

    private static boolean isQueryByEqual(Class beanClass, String fieldCode) {
        try {
            Field field = ReflectUtil.getField(beanClass, fieldCode);
            if (field.getAnnotation(QueryByEqual.class) != null) {
                return true;
            }
        } catch (Exception e) {

        }
        return false;
    }

}
