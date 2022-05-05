package com.fontana.base.exception;

/**
 * 无效的实体对象字段的自定义异常。
 *
 * @author cqf
 * @date 2021-08-08
 */
public class InvalidDataFieldException extends GeneralException {

    /**
     * 构造函数。
     *
     * @param modelName 实体对象名。
     * @param fieldName 字段名。
     */
    public InvalidDataFieldException(String modelName, String fieldName) {
        super("Invalid FieldName [" + fieldName + "] in Model Class [" + modelName + "].");
    }
}
