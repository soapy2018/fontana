package com.fontana.xxljob.service;

import com.fontana.xxljob.model.XxlJobGroup;

import java.util.List;

public interface JobGroupService {

    /**
     * 获取执行器列表
     * @return
     */
    List<XxlJobGroup> getJobGroup();

    /**
     * 自动注册执行器和任务
     * @return
     */
    boolean autoRegisterGroup();

    /**
     * 判断执行器是否已注册
     * @return
     */
    boolean preciselyCheck();

}
