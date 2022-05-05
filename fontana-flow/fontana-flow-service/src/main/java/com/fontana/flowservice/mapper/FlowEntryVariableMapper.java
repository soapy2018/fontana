package com.fontana.flowservice.mapper;

import com.fontana.db.mapper.BaseDaoMapper;
import com.fontana.flowservice.entity.FlowEntryVariable;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 流程变量数据操作访问接口。
 *
 * @author cqf
 * @date 2021-06-06
 */
public interface FlowEntryVariableMapper extends BaseDaoMapper<FlowEntryVariable> {

    /**
     * 获取过滤后的对象列表。
     *
     * @param flowEntryVariableFilter 主表过滤对象。
     * @param orderBy 排序字符串，order by从句的参数。
     * @return 对象列表。
     */
    List<FlowEntryVariable> getFlowEntryVariableList(
            @Param("flowEntryVariableFilter") FlowEntryVariable flowEntryVariableFilter,
            @Param("orderBy") String orderBy);
}
