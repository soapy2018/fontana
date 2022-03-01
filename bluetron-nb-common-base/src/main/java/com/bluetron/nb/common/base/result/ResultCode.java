package com.bluetron.nb.common.base.result;

import com.bluetron.nb.common.base.constant.ICodeAndMessageEnum;

/**
 * @author chenqingfeng
 */

public enum ResultCode implements ICodeAndMessageEnum {

    /* 成功状态码 */
    //SUCCESS(1, "成功","SUCCESS"),
    SUCCESS(1, "成功"),
    /* 默认失败状态码 */
    //FAIL(-1, "失败", "fail"),
    FAIL(-1, "失败"),
    /* 参数错误：10001-19999 */
//    PARAM_IS_INVALID(10001, "参数无效", "param_is_invalid"),
//    PARAM_IS_BLANK(10002, "参数为空", "param_is_blank"),
//    PARAM_TYPE_BIND_ERROR(10003, "参数类型错误", "param_type_bind_error"),
//    PARAM_NOT_COMPLETE(10004, "参数缺失", "param_not_complete"),

    PARAM_IS_INVALID(10001, "参数无效"),
    PARAM_IS_BLANK(10002, "参数为空"),
    PARAM_TYPE_BIND_ERROR(10003, "参数类型错误"),
    PARAM_NOT_COMPLETE(10004, "参数缺失"),

    /* 用户错误：20001-29999*/
//    USER_NOT_LOGGED_IN(20001, "用户未登录", "user_not_logged_in"),
//    USER_LOGIN_ERROR(20002, "账号不存在或密码错误", "user_login_error"),
//    USER_ACCOUNT_FORBIDDEN(20003, "账号已被禁用", "user_account_forbidden"),
//    USER_NOT_EXIST(20004, "用户不存在", "user_not_exist"),
//    USER_HAS_EXISTED(20005, "用户已存在", "user_has_existed"),
//    USER_FACTORY_DISCONTINUATION(20100, "工厂已停用", "user_factory_discontinuation"),

    USER_NOT_LOGGED_IN(20001, "用户未登录"),
    USER_LOGIN_ERROR(20002, "账号不存在或密码错误"),
    USER_ACCOUNT_FORBIDDEN(20003, "账号已被禁用"),
    USER_NOT_EXIST(20004, "用户不存在"),
    USER_ALREADY_EXISTED(20005, "用户已存在"),
    USER_ACCOUNT_LOCKED(20006, "账号已被锁定"),
    USER_FACTORY_DISCONTINUATION(20100, "工厂已停用"),

    /* 业务错误：30001-39999 */
    INVALID_UPLOAD_FILE_IOERROR(30002,"上传文件写入失败，请联系管理员！"),



    /* 系统错误：40001-49999 */
    //SYSTEM_INNER_ERROR(40001, "系统繁忙，请稍后重试", "system_inner_error"),
    SYSTEM_INNER_ERROR(40001, "系统繁忙，请稍后重试"),
    ACQUIRE_CHECK_CODES_ERROR(40002, "获取验证码出错"),

    /* 数据错误：50001-599999 */
    //DATA_NOT_EXIST(50001, "数据未找到", "data_not_exist"),
    DATA_NOT_EXIST(50001, "数据不存在"),
    DATA_IS_WRONG(50002, "数据有误"),
    DATA_ALREADY_EXISTED(50003, "数据已存在"),
    DATA_VALIDATED_FAILED(50004,"数据验证失败"),
    INVALID_RELATED_RECORD_ID(50005,"数据验证失败，关联数据并不存在"),
    DATA_PARENT_ID_NOT_EXIST(50006,"数据验证失败，ParentId不存在"),
    HAS_CHILDREN_DATA(50007,"数据验证失败，子数据存在"),
    INVALID_UPLOAD_FILE_ARGUMENT(50008,"数据验证失败，上传文件参数错误，请核对！"),
    INVALID_UPLOAD_FIELD(50009,"数据验证失败，该字段不支持数据上传！"),
    UPLOAD_FAILED(50010,"数据验证失败，数据上传失败！"),


    /* 接口错误：60001-69999 */
    INTERFACE_INNER_INVOKE_ERROR(60001, "内部系统接口调用异常"),
    INTERFACE_OUTER_INVOKE_ERROR(60002, "外部系统接口调用异常"),
    INTERFACE_FORBID_VISIT(60003, "该接口禁止访问"),
    INTERFACE_ADDRESS_INVALID(60004, "接口地址无效"),
    INTERFACE_REQUEST_TIMEOUT(60005, "接口请求超时"),
    INTERFACE_EXCEED_LOAD(60006, "接口负载过高"),
    RPC_DATA_ACCESS_FAILED(60007, "远程调用数据访问失败"),

    /* 权限错误：70001-79999 */
    NO_ACCESS_PERMISSION(70001, "无访问权限"),
    NO_OPERATION_PERMISSION(70002, "当前用户没有操作权限，请核对！");

    private Integer code;

    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getMessageFromName(String name) {
        for (ResultCode item : ResultCode.values()) {
            if (item.name().equals(name)) {
                return item.message;
            }
        }
        return name;
    }

    public static Integer getCodeFromName(String name) {
        for (ResultCode item : ResultCode.values()) {
            if (item.name().equals(name)) {
                return item.code;
            }
        }
        return null;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public String toString() {
        return this.name();
    }


}
