package com.fontana.sb.exception;

import com.fontana.base.exception.GeneralException;
import com.fontana.base.result.Result;
import com.fontana.sb.i18n.I18nMessageResourceAccessor;
import com.fontana.util.lang.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.SocketTimeoutException;
import java.sql.SQLException;
import java.util.Optional;

/**
 * 异常通用处理
 *
 * @author cqf
 */
@RestControllerAdvice
@Slf4j
public class DefaultExceptionAdvice {
    @Autowired
    I18nMessageResourceAccessor messageResource;

    /**
     * IllegalArgumentException异常处理返回json
     * 返回状态码:400
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class})
    public Result handleIllegalArgumentException(IllegalArgumentException e) {
        return defHandler("参数解析失败", e);
    }

    /**
     * 返回状态码:405
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public Result handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return defHandler("不支持当前请求方法", e);
    }

    /**
     * 返回状态码:415
     */
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    public Result handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        return defHandler("不支持当前媒体类型", e);
    }

    /**
     * SQLException sql异常处理
     * 返回状态码:500
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({SQLException.class})
    public Result handleSQLException(SQLException e) {
        return defHandler("服务运行SQLException异常", e);
    }

    /**
     * SocketTimeoutException 超時异常处理
     * 返回状态码:500
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({SocketTimeoutException.class})
    public Result handleSocketTimeoutException(SocketTimeoutException e) {
        return defHandler("超时SocketTimeoutException异常", e);
    }

    /**
     * SocketTimeoutException 超時异常处理
     * 返回状态码:500
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({ArithmeticException.class})
    public Result handleArithmeticException(ArithmeticException e) {
        return defHandler("算数计算异常", e);
    }


    /**
     * GeneralException 自定义异常处理基础类
     * 返回状态码:500
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(GeneralException.class)
    public Result handleGeneralException(GeneralException e) {
        return defHandler(e);
    }

    /**
     * 所有异常统一处理
     * 返回状态码:500
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        return defHandler("未知异常", e);
    }

    private Result defHandler(String msg, Exception e) {
        log.error(msg, e);
        return Result.failed(msg);
    }

    private Result defHandler(GeneralException e) {

        String message =  messageResource.getMessage(e.getKey());
        if(StringUtil.isEmpty(message)) {
            message = e.getMessage();
        }
        log.error("Request error, code={}, message={}",e.getResultCode().getCode(), message , e);
        return Result.failed(e.getResultCode());
    }
}
