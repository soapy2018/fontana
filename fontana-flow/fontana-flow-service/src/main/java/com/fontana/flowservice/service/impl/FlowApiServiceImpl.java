package com.fontana.flowservice.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fontana.base.object.TokenData;
import com.fontana.base.result.CallResult;
import com.fontana.base.result.Pagination;
import com.fontana.db.object.MyPageParam;
import com.fontana.flowapi.constant.FlowConstant;
import com.fontana.flowapi.dict.FlowApprovalType;
import com.fontana.flowapi.dict.FlowTaskStatus;
import com.fontana.flowapi.vo.FlowTaskVo;
import com.fontana.flowservice.entity.FlowEntryPublish;
import com.fontana.flowservice.entity.FlowTaskComment;
import com.fontana.flowservice.entity.FlowTaskExt;
import com.fontana.flowservice.service.FlowApiService;
import com.fontana.flowservice.service.FlowWorkOrderService;
import com.fontana.flowservice.util.BaseFlowDeptPostExtHelper;
import com.fontana.flowservice.util.FlowCustomExtFactory;
import com.fontana.util.lang.StringUtil;
import com.fontana.util.request.WebContextUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.*;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service("flowApiService")
public class FlowApiServiceImpl implements FlowApiService {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private com.fontana.flowservice.service.FlowEntryService flowEntryService;
    @Autowired
    private com.fontana.flowservice.service.FlowTaskCommentService flowTaskCommentService;
    @Autowired
    private com.fontana.flowservice.service.FlowTaskExtService flowTaskExtService;
    @Autowired
    private FlowWorkOrderService flowWorkOrderService;
    @Autowired
    private FlowCustomExtFactory flowCustomExtFactory;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void start(String processDefinitionId) {
        String loginName = WebContextUtil.takeTokenFromRequest().getLoginName();
        Map<String, Object> variableMap = new HashMap<>(4);
        variableMap.put(FlowConstant.PROC_INSTANCE_INITIATOR_VAR, loginName);
        variableMap.put(FlowConstant.PROC_INSTANCE_START_USER_NAME_VAR, loginName);
        Authentication.setAuthenticatedUserId(loginName);
        runtimeService.startProcessInstanceById(processDefinitionId, null, variableMap);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ProcessInstance startAndTakeFirst(
            String processDefinitionId, Object dataId, FlowTaskComment flowTaskComment, JSONObject taskVariableData) {
        String loginName = WebContextUtil.takeTokenFromRequest().getLoginName();
        Authentication.setAuthenticatedUserId(loginName);
        // ?????????????????????
        Map<String, Object> variableMap = this.initAndGetProcessInstanceVariables(processDefinitionId);
        // ?????????????????????????????????????????????????????????????????????businessKey?????????????????????????????????
        ProcessInstance instance = runtimeService.startProcessInstanceById(
                processDefinitionId, dataId.toString(), variableMap);
        // ??????????????????????????????????????????
        Task task = taskService.createTaskQuery().processInstanceId(instance.getId()).active().singleResult();
        if (StrUtil.equalsAny(task.getAssignee(), loginName, FlowConstant.START_USER_NAME_VAR)) {
            // ??????????????????????????????????????????????????????????????????assignee??????????????????????????????complete???
            flowTaskComment.fillWith(task);
            this.completeTask(task, flowTaskComment, taskVariableData);
        }
        return instance;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void submitConsign(HistoricTaskInstance startTaskInstance, Task multiInstanceActiveTask, String newAssignees) {
        JSONArray assigneeArray = JSON.parseArray(newAssignees);
        for (int i = 0; i < assigneeArray.size(); i++) {
            Map<String, Object> variables = new HashMap<>(2);
            variables.put("assignee", assigneeArray.getString(i));
            variables.put(FlowConstant.MULTI_SIGN_START_TASK_VAR, startTaskInstance.getId());
            runtimeService.addMultiInstanceExecution(
                    multiInstanceActiveTask.getTaskDefinitionKey(), multiInstanceActiveTask.getProcessInstanceId(), variables);
        }
        FlowTaskComment flowTaskComment = new FlowTaskComment();
        flowTaskComment.fillWith(startTaskInstance);
        flowTaskComment.setApprovalType(FlowApprovalType.MULTI_CONSIGN);
        String loginName = WebContextUtil.takeTokenFromRequest().getLoginName();
        String comment = String.format("?????? [%s] ?????? [%s]???", loginName, newAssignees);
        flowTaskComment.setComment(comment);
        flowTaskCommentService.saveNew(flowTaskComment);
        return;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void completeTask(Task task, FlowTaskComment flowTaskComment, JSONObject taskVariableData) {
        if (flowTaskComment != null) {
            // ????????????????????????????????????
            if (flowTaskComment.getApprovalType().equals(FlowApprovalType.MULTI_SIGN)) {
                String loginName = WebContextUtil.takeTokenFromRequest().getLoginName();
                String assigneeList = taskVariableData.getString(FlowConstant.MULTI_ASSIGNEE_LIST_VAR);
                Assert.notNull(taskVariableData);
                Assert.notNull(assigneeList);
                taskVariableData.put(FlowConstant.MULTI_AGREE_COUNT_VAR, 0);
                taskVariableData.put(FlowConstant.MULTI_REFUSE_COUNT_VAR, 0);
                taskVariableData.put(FlowConstant.MULTI_ABSTAIN_COUNT_VAR, 0);
                taskVariableData.put(FlowConstant.MULTI_SIGN_NUM_OF_INSTANCES_VAR, 0);
                taskVariableData.put(FlowConstant.MULTI_SIGN_START_TASK_VAR, task.getId());
                String comment = String.format("?????? [%s] ?????? [%s]???", loginName, assigneeList);
                flowTaskComment.setComment(comment);
            }
            // ???????????????
            if (FlowApprovalType.TRANSFER.equals(flowTaskComment.getApprovalType())) {
                taskService.setAssignee(task.getId(), flowTaskComment.getDelegateAssginee());
                flowTaskComment.fillWith(task);
                flowTaskCommentService.saveNew(flowTaskComment);
                return;
            }
            if (taskVariableData == null) {
                taskVariableData = new JSONObject();
            }
            this.handleMultiInstanceApprovalType(
                    task.getExecutionId(), flowTaskComment.getApprovalType(), taskVariableData);
            taskVariableData.put(FlowConstant.OPERATION_TYPE_VAR, flowTaskComment.getApprovalType());
            taskService.complete(task.getId(), taskVariableData);
            flowTaskComment.fillWith(task);
            flowTaskCommentService.saveNew(flowTaskComment);
        } else {
            taskService.complete(task.getId(), taskVariableData);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CallResult verifyAssigneeOrCandidateAndClaim(Task task) {
        String errorMessage;
        String loginName = WebContextUtil.takeTokenFromRequest().getLoginName();
        // ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
        if (task.getAssignee() == null) {
            // ???????????????
            if (!this.isAssigneeOrCandidate(task)) {
                errorMessage = "??????????????????????????????????????????????????????????????????????????????????????????";
                return CallResult.error(errorMessage);
            }
            // ????????????????????????????????????
            taskService.claim(task.getId(), loginName);
        } else {
            if (!task.getAssignee().equals(loginName)) {
                errorMessage = "??????????????????????????????????????????????????????????????????????????????????????????";
                return CallResult.error(errorMessage);
            }
        }
        return CallResult.ok();
    }

    @Override
    public Map<String, Object> initAndGetProcessInstanceVariables(String processDefinitionId) {
        TokenData tokenData = WebContextUtil.takeTokenFromRequest();
        String loginName = tokenData.getLoginName();
        // ?????????????????????
        Map<String, Object> variableMap = new HashMap<>(4);
        variableMap.put(FlowConstant.PROC_INSTANCE_INITIATOR_VAR, loginName);
        variableMap.put(FlowConstant.PROC_INSTANCE_START_USER_NAME_VAR, loginName);
        List<FlowTaskExt> flowTaskExtList = flowTaskExtService.getByProcessDefinitionId(processDefinitionId);
        boolean hasDeptPostLeader = false;
        boolean hasUpDeptPostLeader = false;
        for (FlowTaskExt flowTaskExt : flowTaskExtList) {
            if (StrUtil.equals(flowTaskExt.getGroupType(), FlowConstant.GROUP_TYPE_UP_DEPT_POST_LEADER)) {
                hasUpDeptPostLeader = true;
            } else if (StrUtil.equals(flowTaskExt.getGroupType(), FlowConstant.GROUP_TYPE_DEPT_POST_LEADER)) {
                hasDeptPostLeader = true;
            }
        }
        // ????????????????????????????????????????????????????????????(???????????????????????????????????????)???flowDeptPostExtHelper????????????null???
        // ??????????????????????????? BaseFlowDeptPostExtHelper ?????????????????????FlowCustomExtFactory???????????????
        BaseFlowDeptPostExtHelper flowDeptPostExtHelper = flowCustomExtFactory.getFlowDeptPostExtHelper();
        if (hasUpDeptPostLeader) {
            Assert.notNull(flowDeptPostExtHelper);
            Long upLeaderDeptPostId = flowDeptPostExtHelper.getUpLeaderDeptPostId(tokenData.getDeptId());
            variableMap.put(FlowConstant.GROUP_TYPE_UP_DEPT_POST_LEADER_VAR, upLeaderDeptPostId);
        }
        if (hasDeptPostLeader) {
            Assert.notNull(flowDeptPostExtHelper);
            Long leaderDeptPostId = flowDeptPostExtHelper.getLeaderDeptPostId(tokenData.getDeptId());
            variableMap.put(FlowConstant.GROUP_TYPE_DEPT_POST_LEADER_VAR, leaderDeptPostId);
        }
        return variableMap;
    }

    @Override
    public boolean isAssigneeOrCandidate(TaskInfo task) {
        String loginName = WebContextUtil.takeTokenFromRequest().getLoginName();
        if (StrUtil.isNotBlank(task.getAssignee())) {
            return StrUtil.equals(loginName, task.getAssignee());
        }
        TaskQuery query = taskService.createTaskQuery();
        this.buildCandidateCondition(query, loginName);
        return query.active().count() != 0;
    }

    @Override
    public boolean isProcessInstanceStarter(String processInstanceId) {
        String loginName = WebContextUtil.takeTokenFromRequest().getLoginName();
        return historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId).startedBy(loginName).count() != 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void setBusinessKeyForProcessInstance(String processInstanceId, Object dataId) {
        runtimeService.updateBusinessKey(processInstanceId, dataId.toString());
    }

    @Override
    public boolean existActiveProcessInstance(String processInstanceId) {
        return runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId).active().count() != 0;
    }

    @Override
    public ProcessInstance getProcessInstance(String processInstanceId) {
        return runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
    }

    @Override
    public Task getProcessInstanceActiveTask(String processInstanceId, String taskId) {
        TaskQuery query = taskService.createTaskQuery().processInstanceId(processInstanceId);
        if (StrUtil.isNotBlank(taskId)) {
            query.taskId(taskId);
        }
        return query.active().singleResult();
    }

    @Override
    public List<Task> getProcessInstanceActiveTaskList(String processInstanceId) {
        return taskService.createTaskQuery().processInstanceId(processInstanceId).list();
    }

    @Override
    public Pagination<Task> getTaskListByUserName(String username, String definitionKey, MyPageParam pageParam) {
        TaskQuery query = taskService.createTaskQuery().active();
        if (StrUtil.isNotBlank(definitionKey)) {
            query.processDefinitionKey(definitionKey);
        }
        this.buildCandidateCondition(query, username);
        query.orderByTaskCreateTime().desc();
        long totalCount = query.count();
        int firstResult = (pageParam.getPageNum() - 1) * pageParam.getPageSize();
        List<Task> taskList = query.listPage(firstResult, pageParam.getPageSize());
        return new Pagination<>(taskList, totalCount);
    }

    @Override
    public long getTaskCountByUserName(String username) {
        return taskService.createTaskQuery().taskCandidateOrAssigned(username).active().count();
    }

    @Override
    public List<Task> getTaskListByProcessInstanceIds(List<String> processInstanceIdSet) {
        return taskService.createTaskQuery().processInstanceIdIn(processInstanceIdSet).active().list();
    }

    @Override
    public List<ProcessInstance> getProcessInstanceList(Set<String> processInstanceIdSet) {
        return runtimeService.createProcessInstanceQuery().processInstanceIds(processInstanceIdSet).list();
    }

    @Override
    public List<ProcessDefinition> getProcessDefinitionList(Set<String> processDefinitionIdSet) {
        return repositoryService.createProcessDefinitionQuery().processDefinitionIds(processDefinitionIdSet).list();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void suspendProcessDefinition(String processDefinitionId) {
        repositoryService.suspendProcessDefinitionById(processDefinitionId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void activateProcessDefinition(String processDefinitionId) {
        repositoryService.activateProcessDefinitionById(processDefinitionId);
    }

    @Override
    public BpmnModel getBpmnModelByDefinitionId(String processDefinitionId) {
        return repositoryService.getBpmnModel(processDefinitionId);
    }

    @Override
    public ProcessDefinition getProcessDefinitionByDeployId(String deployId) {
        return repositoryService.createProcessDefinitionQuery().deploymentId(deployId).singleResult();
    }

    @Override
    public Object getProcessInstanceVariable(String processInstanceId, String variableName) {
        return runtimeService.getVariable(processInstanceId, variableName);
    }

    @Override
    public List<FlowTaskVo> convertToFlowTaskList(List<Task> taskList) {
        List<FlowTaskVo> flowTaskVoList = new LinkedList<>();
        if (CollUtil.isEmpty(taskList)) {
            return flowTaskVoList;
        }
        Set<String> processDefinitionIdSet = taskList.stream()
                .map(Task::getProcessDefinitionId).collect(Collectors.toSet());
        Set<String> procInstanceIdSet = taskList.stream()
                .map(Task::getProcessInstanceId).collect(Collectors.toSet());
        List<FlowEntryPublish> flowEntryPublishList =
                flowEntryService.getFlowEntryPublishList(processDefinitionIdSet);
        Map<String, FlowEntryPublish> flowEntryPublishMap =
                flowEntryPublishList.stream().collect(Collectors.toMap(FlowEntryPublish::getProcessDefinitionId, c -> c));
        List<ProcessInstance> instanceList = this.getProcessInstanceList(procInstanceIdSet);
        Map<String, ProcessInstance> instanceMap =
                instanceList.stream().collect(Collectors.toMap(ProcessInstance::getId, c -> c));
        List<ProcessDefinition> definitionList = this.getProcessDefinitionList(processDefinitionIdSet);
        Map<String, ProcessDefinition> definitionMap =
                definitionList.stream().collect(Collectors.toMap(ProcessDefinition::getId, c -> c));
        for (Task task : taskList) {
            FlowTaskVo flowTaskVo = new FlowTaskVo();
            flowTaskVo.setTaskId(task.getId());
            flowTaskVo.setTaskName(task.getName());
            flowTaskVo.setTaskKey(task.getTaskDefinitionKey());
            flowTaskVo.setTaskFormKey(task.getFormKey());
            flowTaskVo.setEntryId(flowEntryPublishMap.get(task.getProcessDefinitionId()).getEntryId());
            ProcessDefinition processDefinition = definitionMap.get(task.getProcessDefinitionId());
            flowTaskVo.setProcessDefinitionId(processDefinition.getId());
            flowTaskVo.setProcessDefinitionName(processDefinition.getName());
            flowTaskVo.setProcessDefinitionKey(processDefinition.getKey());
            flowTaskVo.setProcessDefinitionVersion(processDefinition.getVersion());
            ProcessInstance processInstance = instanceMap.get(task.getProcessInstanceId());
            flowTaskVo.setProcessInstanceId(processInstance.getId());
            Object initiator = this.getProcessInstanceVariable(
                    processInstance.getId(), FlowConstant.PROC_INSTANCE_INITIATOR_VAR);
            flowTaskVo.setProcessInstanceInitiator(initiator.toString());
            flowTaskVo.setProcessInstanceStartTime(processInstance.getStartTime());
            flowTaskVoList.add(flowTaskVo);
        }
        return flowTaskVoList;
    }

    @Override
    public void addProcessInstanceEndListener(BpmnModel bpmnModel, Class<? extends ExecutionListener> listenerClazz) {
        Assert.notNull(listenerClazz);
        Process process = bpmnModel.getMainProcess();
        FlowableListener activitiListener = new FlowableListener();
        activitiListener.setEvent("end");
        activitiListener.setImplementationType("class");
        activitiListener.setImplementation(listenerClazz.getName());
        process.getExecutionListeners().add(activitiListener);
    }

    @Override
    public void addTaskCreateListener(UserTask userTask, Class<? extends TaskListener> listenerClazz) {
        Assert.notNull(listenerClazz);
        FlowableListener activitiListener = new FlowableListener();
        activitiListener.setEvent("create");
        activitiListener.setImplementationType("class");
        activitiListener.setImplementation(listenerClazz.getName());
        userTask.getTaskListeners().add(activitiListener);
    }

    @Override
    public HistoricProcessInstance getHistoricProcessInstance(String processInstanceId) {
        return historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
    }

    @Override
    public List<HistoricProcessInstance> getHistoricProcessInstanceList(Set<String> processInstanceIdSet) {
        return historyService.createHistoricProcessInstanceQuery().processInstanceIds(processInstanceIdSet).list();
    }

    @Override
    public Pagination<HistoricProcessInstance> getHistoricProcessInstanceList(
            String processDefinitionKey,
            String processDefinitionName,
            String startUser,
            String beginDate,
            String endDate,
            MyPageParam pageParam,
            boolean finishedOnly) throws ParseException {
        HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery();
        if (StrUtil.isNotBlank(processDefinitionKey)) {
            query.processDefinitionKey(processDefinitionKey);
        }
        if (StrUtil.isNotBlank(processDefinitionName)) {
            query.processDefinitionName(processDefinitionName);
        }
        if (StrUtil.isNotBlank(startUser)) {
            query.startedBy(startUser);
        }
        if (StrUtil.isNotBlank(beginDate)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            query.startedAfter(sdf.parse(beginDate));
        }
        if (StrUtil.isNotBlank(endDate)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            query.startedBefore(sdf.parse(endDate));
        }
        if (finishedOnly) {
            query.finished();
        }
        query.orderByProcessInstanceStartTime().desc();
        long totalCount = query.count();
        int firstResult = (pageParam.getPageNum() - 1) * pageParam.getPageSize();
        List<HistoricProcessInstance> instanceList = query.listPage(firstResult, pageParam.getPageSize());
        return new Pagination<>(instanceList, totalCount);
    }

    @Override
    public Pagination<HistoricTaskInstance> getHistoricTaskInstanceFinishedList(
            String processDefinitionName,
            String beginDate,
            String endDate,
            MyPageParam pageParam) throws ParseException {
        String loginName = WebContextUtil.takeTokenFromRequest().getLoginName();
        HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(loginName)
                .finished();
        if (StrUtil.isNotBlank(processDefinitionName)) {
            query.processDefinitionName(processDefinitionName);
        }
        if (StrUtil.isNotBlank(beginDate)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            query.taskCompletedAfter(sdf.parse(beginDate));
        }
        if (StrUtil.isNotBlank(endDate)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            query.taskCompletedBefore(sdf.parse(endDate));
        }
        query.orderByHistoricTaskInstanceEndTime().desc();
        long totalCount = query.count();
        int firstResult = (pageParam.getPageNum() - 1) * pageParam.getPageSize();
        List<HistoricTaskInstance> instanceList = query.listPage(firstResult, pageParam.getPageSize());
        return new Pagination<>(instanceList, totalCount);
    }

    @Override
    public List<HistoricActivityInstance> getHistoricActivityInstanceList(String processInstanceId) {
        return historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).list();
    }

    @Override
    public HistoricTaskInstance getHistoricTaskInstance(String processInstanceId, String taskId) {
        return historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId).taskId(taskId).singleResult();
    }

    @Override
    public List<HistoricActivityInstance> getHistoricUnfinishedInstanceList(String processInstanceId) {
        return historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId).unfinished().list();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CallResult stopProcessInstance(String processInstanceId, String stopReason, boolean forCancel) {
        List<Task> taskList = taskService.createTaskQuery().processInstanceId(processInstanceId).active().list();
        if (CollUtil.isEmpty(taskList)) {
            return CallResult.error("???????????????????????????????????????????????????????????????");
        }
        for (Task task : taskList) {
            String currActivityId = task.getTaskDefinitionKey();
            BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
            FlowNode currFlow = (FlowNode) bpmnModel.getMainProcess().getFlowElement(currActivityId);
            if (currFlow == null) {
                List<SubProcess> subProcessList =
                        bpmnModel.getMainProcess().findFlowElementsOfType(SubProcess.class);
                for (SubProcess subProcess : subProcessList) {
                    FlowElement flowElement = subProcess.getFlowElement(currActivityId);
                    if (flowElement != null) {
                        currFlow = (FlowNode) flowElement;
                        break;
                    }
                }
            }
            EndEvent endEvent =
                    bpmnModel.getMainProcess().findFlowElementsOfType(EndEvent.class, false).get(0);
            if (!(currFlow.getParentContainer().equals(endEvent.getParentContainer()))) {
                return CallResult.error("??????????????????????????????????????????????????????");
            }
            // ??????????????????????????????
            List<SequenceFlow> oriSequenceFlows = Lists.newArrayList();
            oriSequenceFlows.addAll(currFlow.getOutgoingFlows());
            // ?????????????????????
            currFlow.getOutgoingFlows().clear();
            // ??????????????????
            SequenceFlow newSequenceFlow = new SequenceFlow();
            String uuid = UUID.randomUUID().toString().replace("-", "");
            newSequenceFlow.setId(uuid);
            newSequenceFlow.setSourceFlowElement(currFlow);
            newSequenceFlow.setTargetFlowElement(endEvent);
            currFlow.setOutgoingFlows(CollUtil.newArrayList(newSequenceFlow));
            // ????????????????????????????????????
            taskService.complete(task.getId());
            FlowTaskComment taskComment = new FlowTaskComment(task);
            taskComment.setApprovalType(FlowApprovalType.STOP);
            taskComment.setComment(stopReason);
            flowTaskCommentService.saveNew(taskComment);
            // ???????????????????????????
            currFlow.setOutgoingFlows(oriSequenceFlows);
        }
        int status = FlowTaskStatus.STOPPED;
        if (forCancel) {
            status = FlowTaskStatus.CANCELLED;
        }
        flowWorkOrderService.updateFlowStatusByProcessInstanceId(processInstanceId, status);
        return CallResult.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteProcessInstance(String processInstanceId) {
        historyService.deleteHistoricProcessInstance(processInstanceId);
        flowWorkOrderService.removeByProcessInstanceId(processInstanceId);
    }

    @Override
    public Object getTaskVariable(String taskId, String variableName) {
        return taskService.getVariable(taskId, variableName);
    }

    private void handleMultiInstanceApprovalType(String executionId, String approvalType, JSONObject taskVariableData) {
        if (StrUtil.isBlank(approvalType)) {
            return;
        }
        if (StrUtil.equalsAny(approvalType,
                FlowApprovalType.MULTI_AGREE,
                FlowApprovalType.MULTI_REFUSE,
                FlowApprovalType.MULTI_ABSTAIN)) {
            Map<String, Object> variables = runtimeService.getVariables(executionId);
            Integer agreeCount = (Integer) variables.get(FlowConstant.MULTI_AGREE_COUNT_VAR);
            Integer refuseCount = (Integer) variables.get(FlowConstant.MULTI_REFUSE_COUNT_VAR);
            Integer abstainCount = (Integer) variables.get(FlowConstant.MULTI_ABSTAIN_COUNT_VAR);
            Integer nrOfInstances = (Integer) variables.get(FlowConstant.NUMBER_OF_INSTANCES_VAR);
            taskVariableData.put(FlowConstant.MULTI_AGREE_COUNT_VAR, agreeCount);
            taskVariableData.put(FlowConstant.MULTI_REFUSE_COUNT_VAR, refuseCount);
            taskVariableData.put(FlowConstant.MULTI_ABSTAIN_COUNT_VAR, abstainCount);
            taskVariableData.put(FlowConstant.MULTI_SIGN_NUM_OF_INSTANCES_VAR, nrOfInstances);
            switch (approvalType) {
                case FlowApprovalType.MULTI_AGREE:
                    if (agreeCount == null) {
                        agreeCount = 0;
                    }
                    taskVariableData.put(FlowConstant.MULTI_AGREE_COUNT_VAR, agreeCount + 1);
                    break;
                case FlowApprovalType.MULTI_REFUSE:
                    if (refuseCount == null) {
                        refuseCount = 0;
                    }
                    taskVariableData.put(FlowConstant.MULTI_REFUSE_COUNT_VAR, refuseCount + 1);
                    break;
                case FlowApprovalType.MULTI_ABSTAIN:
                    if (abstainCount == null) {
                        abstainCount = 0;
                    }
                    taskVariableData.put(FlowConstant.MULTI_ABSTAIN_COUNT_VAR, abstainCount + 1);
                    break;
                default:
                    break;
            }
        }
    }

    private void buildCandidateCondition(TaskQuery query, String loginName) {
        Set<String> groupIdSet = new HashSet<>();
        // NOTE: ?????????????????????????????????????????????????????????????????????????????????Id?????????groupIdSet????????????
        // ???????????????????????????Id???????????????Id??????????????????????????????Id????????????????????????????????????
        TokenData tokenData = WebContextUtil.takeTokenFromRequest();
        Object deptId = tokenData.getDeptId();
        if (deptId != null) {
            groupIdSet.add(deptId.toString());
        }
        String deptPostIds = tokenData.getDeptPostIds();
        if (StrUtil.isNotBlank(deptPostIds)) {
            groupIdSet.addAll(Arrays.asList(StringUtil.split(deptPostIds, ",")));
        }
        if (CollUtil.isNotEmpty(groupIdSet)) {
            query.or().taskCandidateGroupIn(groupIdSet).taskCandidateOrAssigned(loginName).endOr();
        } else {
            query.taskCandidateOrAssigned(loginName);
        }
    }
}
