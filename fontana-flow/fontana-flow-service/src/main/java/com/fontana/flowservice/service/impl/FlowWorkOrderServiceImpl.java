package com.fontana.flowservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fontana.base.object.TokenData;
import com.fontana.db.constant.GlobalDeletedFlag;
import com.fontana.db.mapper.BaseDaoMapper;
import com.fontana.db.object.MyRelationParam;
import com.fontana.db.service.impl.AbstractBaseService;
import com.fontana.flowapi.dict.FlowTaskStatus;
import com.fontana.flowservice.entity.FlowWorkOrder;
import com.fontana.flowservice.mapper.FlowWorkOrderMapper;
import com.fontana.flowservice.service.FlowWorkOrderService;
import com.fontana.util.request.WebContextUtil;
import com.fontana.util.tools.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 工作流工单表数据操作服务类。
 *
 * @author cqf
 * @date 2021-06-06
 */
@Slf4j
@Service("flowWorkOrderService")
public class FlowWorkOrderServiceImpl extends AbstractBaseService<FlowWorkOrder, Long> implements FlowWorkOrderService {

    @Autowired
    private FlowWorkOrderMapper flowWorkOrderMapper;

    /**
     * 返回当前Service的主表Mapper对象。
     *
     * @return 主表Mapper对象。
     */
    @Override
    protected BaseDaoMapper<FlowWorkOrder> mapper() {
        return flowWorkOrderMapper;
    }

    /**
     * 保存新增对象。
     *
     * @param instance      流程实例对象。
     * @param dataId        流程实例的BusinessKey。
     * @param onlineTableId 在线数据表的主键Id。
     * @return 新增的工作流工单对象。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public FlowWorkOrder saveNew(ProcessInstance instance, Object dataId, Long onlineTableId) {
        TokenData tokenData = WebContextUtil.takeTokenFromRequest();
        Date now = new Date();
        FlowWorkOrder flowWorkOrder = new FlowWorkOrder();
        flowWorkOrder.setWorkOrderId(IdUtil.nextLongId());
        flowWorkOrder.setProcessDefinitionKey(instance.getProcessDefinitionKey());
        flowWorkOrder.setProcessDefinitionName(instance.getProcessDefinitionName());
        flowWorkOrder.setProcessDefinitionId(instance.getProcessDefinitionId());
        flowWorkOrder.setProcessInstanceId(instance.getId());
        flowWorkOrder.setBusinessKey(dataId.toString());
        flowWorkOrder.setOnlineTableId(onlineTableId);
        flowWorkOrder.setFlowStatus(FlowTaskStatus.SUBMITTED);
        flowWorkOrder.setSubmitUsername(tokenData.getLoginName());
        flowWorkOrder.setDeptId(tokenData.getDeptId());
        flowWorkOrder.setCreateUserId(tokenData.getUserId());
        flowWorkOrder.setUpdateUserId(tokenData.getUserId());
        flowWorkOrder.setCreateTime(now);
        flowWorkOrder.setUpdateTime(now);
        flowWorkOrder.setDeletedFlag(GlobalDeletedFlag.NORMAL);
        flowWorkOrderMapper.insert(flowWorkOrder);
        return flowWorkOrder;
    }

    /**
     * 删除指定数据。
     *
     * @param workOrderId 主键Id。
     * @return 成功返回true，否则false。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean remove(Long workOrderId) {
        return flowWorkOrderMapper.deleteById(workOrderId) == 1;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeByProcessInstanceId(String processInstanceId) {
        FlowWorkOrder filter = new FlowWorkOrder();
        filter.setProcessInstanceId(processInstanceId);
        super.removeBy(filter);
    }

    @Override
    public List<FlowWorkOrder> getFlowWorkOrderList(FlowWorkOrder filter, String orderBy) {
        return flowWorkOrderMapper.getFlowWorkOrderList(filter, orderBy);
    }

    @Override
    public List<FlowWorkOrder> getFlowWorkOrderListWithRelation(FlowWorkOrder filter, String orderBy) {
        List<FlowWorkOrder> resultList = flowWorkOrderMapper.getFlowWorkOrderList(filter, orderBy);
        this.buildRelationForDataList(resultList, MyRelationParam.dictOnly());
        return resultList;
    }

    @Override
    public FlowWorkOrder getFlowWorkOrderByProcessInstanceId(String processInstanceId) {
        FlowWorkOrder filter = new FlowWorkOrder();
        filter.setProcessInstanceId(processInstanceId);
        return flowWorkOrderMapper.selectOne(new QueryWrapper<>(filter));
    }

    @Override
    public boolean existByBusinessKey(Object businessKey, boolean unfinished) {
        LambdaQueryWrapper<FlowWorkOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FlowWorkOrder::getBusinessKey, businessKey.toString());
        if (unfinished) {
            queryWrapper.notIn(FlowWorkOrder::getFlowStatus,
                    FlowTaskStatus.FINISHED, FlowTaskStatus.CANCELLED, FlowTaskStatus.STOPPED);
        }
        return flowWorkOrderMapper.selectCount(queryWrapper) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateFlowStatusByBusinessKey(String businessKey, int flowStatus) {
        FlowWorkOrder flowWorkOrder = new FlowWorkOrder();
        flowWorkOrder.setFlowStatus(flowStatus);
        if (FlowTaskStatus.FINISHED != flowStatus) {
            flowWorkOrder.setUpdateTime(new Date());
            flowWorkOrder.setUpdateUserId(WebContextUtil.takeTokenFromRequest().getUserId());
        }
        LambdaQueryWrapper<FlowWorkOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FlowWorkOrder::getBusinessKey, businessKey);
        flowWorkOrderMapper.update(flowWorkOrder, queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateFlowStatusByProcessInstanceId(String processInstanceId, int flowStatus) {
        FlowWorkOrder flowWorkOrder = new FlowWorkOrder();
        flowWorkOrder.setFlowStatus(flowStatus);
        if (FlowTaskStatus.FINISHED != flowStatus) {
            flowWorkOrder.setUpdateTime(new Date());
            flowWorkOrder.setUpdateUserId(WebContextUtil.takeTokenFromRequest().getUserId());
        }
        LambdaQueryWrapper<FlowWorkOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FlowWorkOrder::getProcessInstanceId, processInstanceId);
        flowWorkOrderMapper.update(flowWorkOrder, queryWrapper);
    }
}
