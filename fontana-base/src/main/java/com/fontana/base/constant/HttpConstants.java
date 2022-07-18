package com.fontana.base.constant;

/**
 * @className: HttpConstants
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2021/9/22 10:27
 */
public interface HttpConstants {

    String ACCESS_TOKEN = "accessToken";

    String TENANT_ID = "tenantId";

    /**
     * token请求头名称
     */
    String AUTHORIZATION_HEADER = "Authorization";

    /**
     * 用户名信息头
     */
    String USER_NAME_HEADER = "x-userName";

    /**
     * 用户id信息头
     */
    String USER_ID_HEADER = "x-userId";

    /**
     * 角色信息头
     */
    String ROLE_HEADER = "x-role";

    /**
     * 租户信息头
     */
    String TENANT_ID_HEADER = "x-tenantId";

    /**
     * 工厂信息头
     */
    String FACTORY_ID_HEADER = "x-factoryId";

    /**
     * 信息头Content-Language
     */
    String CONTENT_LANGUAGE_HEADER = "Content-Language";


    // 员工的登录信息，放在header里的key
    String PERSONNEL_AUTH = "PersonnelAuth";

    /**
     * 请求头跟踪id名。
     */
    String TRACE_ID_HEADER = "x-traceId";

    /**
     * 日志链路块id信息头
     */
    String SPAN_ID_HEADER = "x-spanId";

}


