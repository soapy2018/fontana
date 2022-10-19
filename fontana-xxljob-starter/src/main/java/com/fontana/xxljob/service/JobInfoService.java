package com.fontana.xxljob.service;

import com.fontana.xxljob.model.XxlJobInfo;

import java.util.List;

/**
 * @author chenqingfeng
 */
public interface JobInfoService {

    /**
     * 条件查询获取获取任务列表
     * @param jobGroupId
     * @param executorHandler
     * @return
     */
    List<XxlJobInfo> getJobInfo(Integer jobGroupId, String executorHandler);

    /**
     * 添加任务
     * @param xxlJobInfo
     * @return
     */
    Integer addJobInfo(XxlJobInfo xxlJobInfo);

}
