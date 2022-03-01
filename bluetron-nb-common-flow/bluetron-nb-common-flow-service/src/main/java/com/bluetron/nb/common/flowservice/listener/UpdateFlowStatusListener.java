package com.bluetron.nb.common.flowservice.listener;

import cn.hutool.core.util.StrUtil;
import com.bluetron.nb.common.flowapi.dict.FlowTaskStatus;
import com.bluetron.nb.common.flowservice.service.FlowWorkOrderService;
import com.bluetron.nb.common.util.tools.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;

/**
 * 流程实例监听器，在流程实例结束的时候更新流程工单表的审批状态字段。
 *
 * @author cqf
 * @date 2021-06-06
 */
@Slf4j
public class UpdateFlowStatusListener implements ExecutionListener {

    private final FlowWorkOrderService flowWorkOrderService =
            SpringContextHolder.getBean(FlowWorkOrderService.class);

    @Override
    public void notify(DelegateExecution execution) {
        if (!StrUtil.equals("end", execution.getEventName())) {
            return;
        }
        String processInstanceId = execution.getProcessInstanceId();
        flowWorkOrderService.updateFlowStatusByProcessInstanceId(processInstanceId, FlowTaskStatus.FINISHED);
    }
}
