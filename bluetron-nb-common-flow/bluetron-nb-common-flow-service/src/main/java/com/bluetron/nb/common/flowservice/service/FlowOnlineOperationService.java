package com.bluetron.nb.common.flowservice.service;

import com.alibaba.fastjson.JSONObject;
import com.bluetron.nb.common.flowservice.entity.FlowTaskComment;
import com.bluetron.nb.common.onlineservice.entity.OnlineDatasourceRelation;
import com.bluetron.nb.common.onlineservice.entity.OnlineTable;
import com.bluetron.nb.common.onlineservice.object.ColumnData;
import org.flowable.task.api.Task;

import java.util.List;
import java.util.Map;

/**
 * 流程操作服务接口。
 *
 * @author cqf
 * @date 2021-06-06
 */
public interface FlowOnlineOperationService {

    /**
     * 保存在线表单的数据，同时启动流程。如果当前用户是第一个用户任务的Assignee，
     * 或者第一个用户任务的Assignee是流程发起人变量，该方法还会自动Take第一个任务。
     *
     * @param processDefinitionId 流程定义Id。
     * @param flowTaskComment     流程审批批注对象。
     * @param taskVariableData    流程任务的变量数据。
     * @param table               表对象。
     * @param columnDataList      表数据。
     */
    void saveNewAndStartProcess(
            String processDefinitionId,
            FlowTaskComment flowTaskComment,
            JSONObject taskVariableData,
            OnlineTable table,
            List<ColumnData> columnDataList);

    /**
     * 保存在线表单的数据，同时启动流程。如果当前用户是第一个用户任务的Assignee，
     * 或者第一个用户任务的Assignee是流程发起人变量，该方法还会自动Take第一个任务。
     *
     * @param processDefinitionId    流程定义Id。
     * @param flowTaskComment        流程审批批注对象。
     * @param taskVariableData       流程任务的变量数据。
     * @param masterTable            主表对象。
     * @param masterColumnDataList   主表数据。
     * @param slaveColumnDataListMap 关联从表数据Map。
     */
    void saveNewAndStartProcess(
            String processDefinitionId,
            FlowTaskComment flowTaskComment,
            JSONObject taskVariableData,
            OnlineTable masterTable,
            List<ColumnData> masterColumnDataList,
            Map<OnlineDatasourceRelation, List<List<ColumnData>>> slaveColumnDataListMap);

    /**
     * 保存在线表单的数据，同时Take用户任务。
     *
     * @param processInstanceId 流程实例Id。
     * @param taskId            流程任务Id。
     * @param flowTaskComment   流程审批批注对象。
     * @param taskVariableData  流程任务的变量数据。
     * @param table             表对象。
     * @param columnDataList    表数据。
     */
    void saveNewAndTakeTask(
            String processInstanceId,
            String taskId,
            FlowTaskComment flowTaskComment,
            JSONObject taskVariableData,
            OnlineTable table,
            List<ColumnData> columnDataList);

    /**
     * 保存在线表单的数据，同时Take用户任务。
     *
     * @param processInstanceId      流程实例Id。
     * @param taskId                 流程任务Id。
     * @param flowTaskComment        流程审批批注对象。
     * @param taskVariableData       流程任务的变量数据。
     * @param masterTable            主表对象。
     * @param masterColumnDataList   主表数据。
     * @param slaveColumnDataListMap 关联从表数据Map。
     */
    void saveNewAndTakeTask(
            String processInstanceId,
            String taskId,
            FlowTaskComment flowTaskComment,
            JSONObject taskVariableData,
            OnlineTable masterTable,
            List<ColumnData> masterColumnDataList,
            Map<OnlineDatasourceRelation, List<List<ColumnData>>> slaveColumnDataListMap);

    /**
     * 保存业务表数据，同时接收流程任务。
     *
     * @param task             流程任务。
     * @param flowTaskComment  流程审批批注对象。
     * @param taskVariableData 流程任务的变量数据。
     * @param masterTable      主表对象。
     * @param masterData       主表数据。
     * @param masterDataId     主表数据主键。
     * @param slaveData        从表数据。
     * @param datasourceId     在线表所属数据源Id。
     */
    void updateAndTakeTask(
            Task task,
            FlowTaskComment flowTaskComment,
            JSONObject taskVariableData,
            OnlineTable masterTable,
            JSONObject masterData,
            String masterDataId,
            JSONObject slaveData,
            Long datasourceId);
}
