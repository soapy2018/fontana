package com.fontana.base.result;

import com.fontana.base.constant.ICodeAndMessageEnum;
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
    //private boolean success = true;

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
    public static <T> Result<T> failed(ResultCode code, String msg) {
        return of(null, code.getCode(), msg);
    }

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

    /**
     * 根据参数中出错的Result，创建新的错误应答对象。
     *
     * @param errorCause 导致错误原因的应答对象。
     * @return 返回创建的Result实例对象。
     */
    public static <T, E> Result<T> failed(Result<E> errorCause) {
        return of(null, errorCause.code, errorCause.msg);
    }

    public boolean isSuccess(){
        return code == 1;
    }

    public static <T> Result<T> status(boolean flag) {
        return flag ? succeed("操作成功") : failed("操作失败");
    }

}
