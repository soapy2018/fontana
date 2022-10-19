package com.fontana.xxljob.service;

public interface JobLoginService {

    /**
     * 登陆接口
     */
    void login();

    /**
     * 获取登陆后得到cookie
     * @return
     */
    String getCookie();

}
