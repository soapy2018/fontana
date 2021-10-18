package com.bluetron.nb.common.base.exception;

import com.bluetron.nb.common.base.constant.ICodeAndMessageEnum;
import com.bluetron.nb.common.base.result.ResultCode;

/**
 * @author cqf
 * @date 2021/9/26
 */
public class GeneralException extends RuntimeException {

    private ICodeAndMessageEnum resultCode = ResultCode.FAIL;

    public GeneralException(String msg) {
        super(msg);
    }

    public GeneralException(ICodeAndMessageEnum resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    public Integer getCode() {
        return resultCode.getCode();
    }

    public String getKey() {
        return resultCode.name();
    }

    public ICodeAndMessageEnum getResultCode() {
        return resultCode;
    }

}
