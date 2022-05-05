package com.fontana.base.exception;

/**
 * 无效的实体对象的自定义异常。
 * @author: cqf
 * @date: 2021/12/27 17:13
 */
public class InvalidDataModelException extends GeneralException {

    /**
     * 构造函数。
     *
     * @param modelName 实体对象名。
     */
    public InvalidDataModelException(String modelName) {
        super("Invalid Model Class [" + modelName + "].");
    }
}


