package com.bluetron.nb.common.flowservice.mapper;

import com.bluetron.nb.common.db.mapper.BaseDaoMapper;
import com.bluetron.nb.common.flowservice.entity.FlowEntryPublishVariable;

import java.util.List;

/**
 * 数据操作访问接口。
 *
 * @author cqf
 * @date 2021-06-06
 */
public interface FlowEntryPublishVariableMapper extends BaseDaoMapper<FlowEntryPublishVariable> {

    /**
     * 批量插入流程发布的变量列表。
     *
     * @param entryPublishVariableList 流程发布的变量列表。
     */
    void insertList(List<FlowEntryPublishVariable> entryPublishVariableList);
}
