package com.fontana.flowservice.service;

import com.fontana.db.service.IBaseService;
import com.fontana.flowservice.entity.*;

import java.util.List;

/**
 * 流程任务批注数据操作服务接口。
 *
 * @author cqf
 * @date 2021-06-06
 */
public interface FlowTaskCommentService extends IBaseService<FlowTaskComment, Long> {

    /**
     * 保存新增对象。
     *
     * @param flowTaskComment 新增对象。
     * @return 返回新增对象。
     */
    FlowTaskComment saveNew(FlowTaskComment flowTaskComment);

    /**
     * 查询指定流程实例Id下的所有审批任务的批注。
     *
     * @param processInstanceId  流程实例Id。
     * @return 查询结果集。
     */
    List<FlowTaskComment> getFlowTaskCommentList(String processInstanceId);
}
