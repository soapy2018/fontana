package com.bluetron.nb.common.sb.exception;

import com.bluetron.nb.common.base.constant.ICodeAndMessageEnum;

/**
 * @className: CustomerResultCode
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2021/9/27 14:44
 */
public enum CustomerResultCode implements ICodeAndMessageEnum {

    CUSTOMER_DEFINE_ERROR(90001, "自定义系统错误");

    private Integer code;
    private String message;

    CustomerResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}


