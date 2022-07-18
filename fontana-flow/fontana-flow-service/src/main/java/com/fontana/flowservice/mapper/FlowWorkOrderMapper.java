package com.fontana.flowservice.mapper;

import com.fontana.datafilter.annotation.EnableDataPerm;
import com.fontana.db.mapper.BaseDaoMapper;
import com.fontana.flowservice.entity.FlowWorkOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 工作流工单表数据操作访问接口。
 *
 * @author cqf
 * @date 2021-06-06
 */
@EnableDataPerm
public interface FlowWorkOrderMapper extends BaseDaoMapper<FlowWorkOrder> {

    /**
     * 获取过滤后的对象列表。
     *
     * @param flowWorkOrderFilter 主表过滤对象。
     * @param orderBy 排序字符串，order by从句的参数。
     * @return 对象列表。
     */
    List<FlowWorkOrder> getFlowWorkOrderList(
            @Param("flowWorkOrderFilter") FlowWorkOrder flowWorkOrderFilter, @Param("orderBy") String orderBy);
}
