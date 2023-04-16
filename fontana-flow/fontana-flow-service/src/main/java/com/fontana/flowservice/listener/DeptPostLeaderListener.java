package com.fontana.flowservice.listener;

import com.fontana.flowapi.constant.FlowConstant;
import com.fontana.flowservice.service.FlowApiService;
import com.fontana.util.tools.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.task.service.delegate.DelegateTask;

import java.util.Map;

/**
 * 当用户任务的候选组为本部门领导岗位时，该监听器会在任务创建时，获取当前流程实例发起人的部门领导。
 * 并将其指派为当前任务的候选组。
 *
 * @author cqf
 * @date 2021-06-06
 */
@Slf4j
public class DeptPostLeaderListener implements TaskListener {

    private final FlowApiService flowApiService = SpringContextHolder.getBean(FlowApiService.class);

    @Override
    public void notify(DelegateTask delegateTask) {
        HistoricProcessInstance instance =
                flowApiService.getHistoricProcessInstance(delegateTask.getProcessInstanceId());
        Map<String, Object> variables = delegateTask.getVariables();
        if (variables.get(FlowConstant.GROUP_TYPE_DEPT_POST_LEADER_VAR) == null) {
            delegateTask.setAssignee(variables.get(FlowConstant.PROC_INSTANCE_START_USER_NAME_VAR).toString());
        }
    }
}
