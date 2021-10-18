package com.bluetron.nb.common.base.controller;

import com.bluetron.nb.common.base.constant.ICodeAndMessageEnum;
import com.bluetron.nb.common.base.result.Result;

/**
 * @author cqf
 * @date 2021/9/28 13:29
 */
public abstract class BaseController {

    protected <T> Result<T> success() {
        return Result.succeed();

    }

    protected <T> Result<T> success(String msg) {
        return success(null, msg);

    }

    protected <T> Result<T> success(T data) {
        return success(data, null);
    }

    protected <T> Result<T> success(T data, String msg) {
        return Result.succeed(data, msg);
    }

    protected <T> Result<T> fail() {
        return Result.failed();
    }

    protected <T> Result<T> fail(String msg) {
        return fail(null, msg);
    }

    protected <T> Result<T> fail(ICodeAndMessageEnum resultCode) {
        return fail(null, resultCode);
    }

    protected <T> Result<T> fail(T data, String msg) {
        return Result.failed(data, msg);
    }

    protected <T> Result<T> fail(T data, ICodeAndMessageEnum resultCode) {
        return Result.failed(data, resultCode);
    }

}
