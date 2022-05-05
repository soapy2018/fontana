package com.fontana.flowservice.mapper;

import com.fontana.db.mapper.BaseDaoMapper;
import com.fontana.flowservice.entity.FlowTaskExt;

import java.util.List;

/**
 * 流程任务扩展数据操作访问接口。
 *
 * @author cqf
 * @date 2021-06-06
 */
public interface FlowTaskExtMapper extends BaseDaoMapper<FlowTaskExt> {

    /**
     * 批量插入流程任务扩展信息列表。
     *
     * @param flowTaskExtList 流程任务扩展信息列表。
     */
    void insertList(List<FlowTaskExt> flowTaskExtList);
}
