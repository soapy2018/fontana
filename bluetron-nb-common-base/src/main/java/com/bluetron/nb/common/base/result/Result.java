package com.bluetron.nb.common.base.result;

import com.bluetron.nb.common.base.constant.ICodeAndMessageEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * @Author: cqf
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {

    private T data;
    private Integer code;
    private String msg;

    /*
     成功的返回值统一设为1
    */

    public static <T> Result<T> succeed() {
        return of(null, ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage());
    }

    public static <T> Result<T> succeed(T model) {
        return of(model, ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage());
    }

    public static <T> Result<T> succeed(String msg) {
        return succeed(null, msg);
    }

    public static <T> Result<T> succeed(T model, String msg) {
        return of(model, ResultCode.SUCCESS.getCode(), msg);
    }
    /*
     失败的返回值可自行定义，默认为-1
    */
    public static <T> Result<T> failed(T model, ICodeAndMessageEnum resultCode) {
        return of(model, resultCode.getCode(), resultCode.getMessage());
    }

    public static <T> Result<T> failed(ICodeAndMessageEnum resultCode) {
        return failed(null, resultCode);
    }

    /*不允许在这里随意定义失败code，但可以定义失败消息*/
//    public static <T> Result<T> failed(Integer code, String msg) {
//        return of(null, code, msg);
//    }

    public static <T> Result<T> failed() {
        return of(null, ResultCode.FAIL.getCode(), ResultCode.FAIL.getMessage());
    }

    public static <T> Result<T> failed(String msg) {
        return of(null, ResultCode.FAIL.getCode(), msg);
    }

    public static <T> Result<T> failed(T model, String msg) {
        return of(model, ResultCode.FAIL.getCode(), msg);
    }


    public static <T> Result<T> of(T data, Integer code, String msg) {
        return new Result<>(data, code, msg);
    }
}
