package com.fontana.util.validate;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.fontana.base.validate.AddGroup;
import com.fontana.base.validate.UpdateGroup;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @className: ValidateUtil
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2021/12/31 10:02
 */
public class ValidateUtil {

    private static final Validator VALIDATOR;

    static {
        VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * 判断模型对象是否通过校验，没有通过返回具体的校验错误信息。
     *
     * @param model  带校验的model。
     * @param groups Validate绑定的校验组。
     * @return 没有错误返回null，否则返回具体的错误信息。
     */
    public static <T> String getModelValidationError(T model, Class<?>... groups) {
        if (model != null) {
            Set<ConstraintViolation<T>> constraintViolations = VALIDATOR.validate(model, groups);
            if (!constraintViolations.isEmpty()) {
                Iterator<ConstraintViolation<T>> it = constraintViolations.iterator();
                ConstraintViolation<T> constraint = it.next();
                return constraint.getMessage();
            }
        }
        return null;
    }

    /**
     * 判断模型对象是否通过校验，没有通过返回具体的校验错误信息。
     *
     * @param model     带校验的model。
     * @param forUpdate 是否为更新。
     * @return 没有错误返回null，否则返回具体的错误信息。
     */
    public static <T> String getModelValidationError(T model, boolean forUpdate) {
        if (model != null) {
            Set<ConstraintViolation<T>> constraintViolations;
            if (forUpdate) {
                constraintViolations = VALIDATOR.validate(model, Default.class, UpdateGroup.class);
            } else {
                constraintViolations = VALIDATOR.validate(model, Default.class, AddGroup.class);
            }
            if (!constraintViolations.isEmpty()) {
                Iterator<ConstraintViolation<T>> it = constraintViolations.iterator();
                ConstraintViolation<T> constraint = it.next();
                return constraint.getMessage();
            }
        }
        return null;
    }

    /**
     * 判断模型对象是否通过校验，没有通过返回具体的校验错误信息。
     *
     * @param modelList 带校验的model列表。
     * @param groups    Validate绑定的校验组。
     * @return 没有错误返回null，否则返回具体的错误信息。
     */
    public static <T> String getModelValidationError(List<T> modelList, Class<?>... groups) {
        if (CollUtil.isNotEmpty(modelList)) {
            for (T model : modelList) {
                String errorMessage = getModelValidationError(model, groups);
                if (StrUtil.isNotBlank(errorMessage)) {
                    return errorMessage;
                }
            }
        }
        return null;
    }

    /**
     * 判断模型对象是否通过校验，没有通过返回具体的校验错误信息。
     *
     * @param modelList 带校验的model列表。
     * @param forUpdate 是否为更新。
     * @return 没有错误返回null，否则返回具体的错误信息。
     */
    public static <T> String getModelValidationError(List<T> modelList, boolean forUpdate) {
        if (CollUtil.isNotEmpty(modelList)) {
            for (T model : modelList) {
                String errorMessage = getModelValidationError(model, forUpdate);
                if (StrUtil.isNotBlank(errorMessage)) {
                    return errorMessage;
                }
            }
        }
        return null;
    }
}


