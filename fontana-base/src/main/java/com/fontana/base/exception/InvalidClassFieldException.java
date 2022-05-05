package com.fontana.base.exception;

/**
 * 无效的类对象字段的自定义异常。
 *
 * @author cqf
 * @date 2021-08-08
 */
public class InvalidClassFieldException extends GeneralException {


    /**
     * 构造函数。
     *
     * @param className 对象名。
     * @param fieldName 字段名。
     */
    public InvalidClassFieldException(String className, String fieldName) {
        super("Invalid FieldName [" + fieldName + "] in Class [" + className + "].");
    }
}
