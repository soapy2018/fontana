package com.fontana.flowservice.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fontana.base.exception.GeneralException;
import com.fontana.base.result.Result;
import com.fontana.flowapi.dict.FlowApprovalType;
import com.fontana.flowapi.dict.FlowTaskStatus;
import com.fontana.flowservice.entity.FlowTaskComment;
import com.fontana.flowservice.service.FlowApiService;
import com.fontana.flowservice.service.FlowOnlineOperationService;
import com.fontana.flowservice.service.FlowWorkOrderService;
import com.fontana.onlineapi.dict.RelationType;
import com.fontana.onlineservice.entity.OnlineColumn;
import com.fontana.onlineservice.entity.OnlineDatasourceRelation;
import com.fontana.onlineservice.entity.OnlineTable;
import com.fontana.onlineservice.object.ColumnData;
import com.fontana.onlineservice.service.OnlineOperationService;
import com.fontana.onlineservice.util.OnlineOperationHelper;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@Service("flowOnlineOperationService")
public class FlowOnlineOperationServiceImpl implements FlowOnlineOperationService {

    @Autowired
    private FlowApiService flowApiService;
    @Autowired
    private FlowWorkOrderService flowWorkOrderService;
    @Autowired
    private OnlineOperationService onlineOperationService;
    @Autowired
    private OnlineOperationHelper onlineOperationHelper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveNewAndStartProcess(
            String processDefinitionId,
            FlowTaskComment flowTaskComment,
            JSONObject taskVariableData,
            OnlineTable table,
            List<ColumnData> columnDataList) {
        Object dataId = onlineOperationService.saveNew(table, columnDataList);
        ProcessInstance instance =
                flowApiService.startAndTakeFirst(processDefinitionId, dataId, flowTaskComment, taskVariableData);
        flowWorkOrderService.saveNew(instance, dataId, table.getTableId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveNewAndStartProcess(
            String processDefinitionId,
            FlowTaskComment flowTaskComment,
            JSONObject taskVariableData,
            OnlineTable masterTable,
            List<ColumnData> masterColumnDataList,
            Map<OnlineDatasourceRelation, List<List<ColumnData>>> slaveColumnDataListMap) {
        Object dataId = onlineOperationService.saveNewAndSlaveRelation(
                masterTable, masterColumnDataList, slaveColumnDataListMap);
        ProcessInstance instance =
                flowApiService.startAndTakeFirst(processDefinitionId, dataId, flowTaskComment, taskVariableData);
        flowWorkOrderService.saveNew(instance, dataId, masterTable.getTableId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveNewAndTakeTask(
            String processInstanceId,
            String taskId,
            FlowTaskComment flowTaskComment,
            JSONObject taskVariableData,
            OnlineTable table,
            List<ColumnData> columnDataList) {
        Object dataId = onlineOperationService.saveNew(table, columnDataList);
        Task task = flowApiService.getProcessInstanceActiveTask(processInstanceId, taskId);
        flowApiService.setBusinessKeyForProcessInstance(processInstanceId, dataId);
        Map<String, Object> variables =
                flowApiService.initAndGetProcessInstanceVariables(task.getProcessDefinitionId());
        if (taskVariableData == null) {
            taskVariableData = new JSONObject();
        }
        taskVariableData.putAll(variables);
        flowApiService.completeTask(task, flowTaskComment, taskVariableData);
        ProcessInstance instance = flowApiService.getProcessInstance(processInstanceId);
        flowWorkOrderService.saveNew(instance, dataId, table.getTableId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveNewAndTakeTask(
            String processInstanceId,
            String taskId,
            FlowTaskComment flowTaskComment,
            JSONObject taskVariableData,
            OnlineTable masterTable,
            List<ColumnData> masterColumnDataList,
            Map<OnlineDatasourceRelation, List<List<ColumnData>>> slaveColumnDataListMap) {
        Object dataId = onlineOperationService.saveNewAndSlaveRelation(
                masterTable, masterColumnDataList, slaveColumnDataListMap);
        Task task = flowApiService.getProcessInstanceActiveTask(processInstanceId, taskId);
        flowApiService.setBusinessKeyForProcessInstance(processInstanceId, dataId);
        Map<String, Object> variables =
                flowApiService.initAndGetProcessInstanceVariables(task.getProcessDefinitionId());
        if (taskVariableData == null) {
            taskVariableData = new JSONObject();
        }
        taskVariableData.putAll(variables);
        flowApiService.completeTask(task, flowTaskComment, taskVariableData);
        ProcessInstance instance = flowApiService.getProcessInstance(processInstanceId);
        flowWorkOrderService.saveNew(instance, dataId, masterTable.getTableId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateAndTakeTask(
            Task task,
            FlowTaskComment flowTaskComment,
            JSONObject taskVariableData,
            OnlineTable masterTable,
            JSONObject masterData,
            String masterDataId,
            JSONObject slaveData,
            Long datasourceId) {
        int flowStatus = FlowTaskStatus.APPROVING;
        if (flowTaskComment.getApprovalType().equals(FlowApprovalType.REFUSE)) {
            flowStatus = FlowTaskStatus.REFUSED;
        }
        flowWorkOrderService.updateFlowStatusByProcessInstanceId(task.getProcessInstanceId(), flowStatus);
        this.handleMasterTableData(masterTable, masterData, masterDataId);
        if (slaveData != null) {
            for (Map.Entry<String, Object> relationEntry : slaveData.entrySet()) {
                Long relationId = Long.parseLong(relationEntry.getKey());
                this.handleSlaveTableData(
                        relationId, relationEntry.getValue(), datasourceId, masterTable, masterData, masterDataId);
            }
        }
        flowApiService.completeTask(task, flowTaskComment, taskVariableData);
    }

    private void handleMasterTableData(OnlineTable masterTable, JSONObject masterData, String dataId) {
        // 如果存在主表数据，就执行主表数据的更新。
        if (masterData != null) {
            Map<String, Object> originalMasterData =
                    onlineOperationService.getMasterData(masterTable, null, null, dataId);
            for (Map.Entry<String, Object> entry : originalMasterData.entrySet()) {
                masterData.putIfAbsent(entry.getKey(), entry.getValue());
            }
            Result<List<ColumnData>> columnDataListResult =
                    onlineOperationHelper.buildTableData(masterTable, masterData, true, null);
            if (!columnDataListResult.isSuccess()) {
                throw new GeneralException(columnDataListResult.getMsg());
            }
            if (!onlineOperationService.update(masterTable, columnDataListResult.getData())) {
                throw new GeneralException("主表数据不存在！");
            }
        }
    }

    private void handleSlaveTableData(
            Long relationId,
            Object slaveData,
            Long datasourceId,
            OnlineTable masterTable,
            Map<String, Object> masterData,
            String masterDataId) {
        Result<OnlineDatasourceRelation> relationResult =
                onlineOperationHelper.verifyAndGetOneToManyRelation(datasourceId, relationId);
        if (!relationResult.isSuccess()) {
            throw new GeneralException(relationResult.getMsg());
        }
        OnlineDatasourceRelation relation = relationResult.getData();
        OnlineTable slaveTable = relation.getSlaveTable();
        if (relation.getRelationType().equals(RelationType.ONE_TO_ONE)) {
            String keyColumnName = slaveTable.getPrimaryKeyColumn().getColumnName();
            JSONObject relationData = (JSONObject) slaveData;
            if (MapUtil.isEmpty(relationData)) {
                return;
            }
            String slaveDataId = relationData.getString(keyColumnName);
            if (slaveDataId == null) {
                Result<List<ColumnData>> columnDataListResult =
                        onlineOperationHelper.buildTableData(slaveTable, relationData, false, null);
                if (!columnDataListResult.isSuccess()) {
                    throw new GeneralException(relationResult.getMsg());
                }
                onlineOperationService.saveNew(slaveTable, columnDataListResult.getData());
            } else {
                Map<String, Object> originalSlaveData =
                        onlineOperationService.getMasterData(slaveTable, null, null, slaveDataId);
                for (Map.Entry<String, Object> entry : originalSlaveData.entrySet()) {
                    relationData.putIfAbsent(entry.getKey(), entry.getValue());
                }
                Result<List<ColumnData>> columnDataListResult =
                        onlineOperationHelper.buildTableData(slaveTable, relationData, true, null);
                if (!columnDataListResult.isSuccess()) {
                    throw new GeneralException(relationResult.getMsg());
                }
                if (!onlineOperationService.update(slaveTable, columnDataListResult.getData())) {
                    throw new GeneralException("关联从表 [" + slaveTable.getTableName() + "] 中的更新数据不存在");
                }
            }
        } else if (relation.getRelationType().equals(RelationType.ONE_TO_MANY)) {
            JSONArray relationDataArray = (JSONArray) slaveData;
            if (CollUtil.isEmpty(relationDataArray)) {
                return;
            }
            JSONObject firstData = relationDataArray.getJSONObject(0);
            Object key = firstData.get(slaveTable.getPrimaryKeyColumn().getColumnName());
            // 如果一对多关联数据中存在主键数据，则需要先批量删除。
            if (key != null) {
                OnlineColumn slaveColumn = relation.getSlaveTable().getColumnMap().get(relation.getSlaveColumnId());
                Object relationSlaveColumnValue = firstData.get(slaveColumn.getColumnName());
                onlineOperationService.forceDelete(
                        relation.getSlaveTable(), slaveColumn, relationSlaveColumnValue.toString());
            }
            if (masterData == null) {
                masterData = onlineOperationService.getMasterData(
                        masterTable, null, null, masterDataId);
            }
            for (int i = 0; i < relationDataArray.size(); i++) {
                JSONObject relationSlaveData = relationDataArray.getJSONObject(i);
                // 自动补齐主表关联数据。
                OnlineColumn masterColumn = masterTable.getColumnMap().get(relation.getMasterColumnId());
                Object masterColumnValue = masterData.get(masterColumn.getColumnName());
                OnlineColumn slaveColumn = slaveTable.getColumnMap().get(relation.getSlaveColumnId());
                relationSlaveData.put(slaveColumn.getColumnName(), masterColumnValue);
                // 拆解主表和一对多关联从表的输入参数，并构建出数据表的待插入数据列表。
                Result<List<ColumnData>> columnDataListResult =
                        onlineOperationHelper.buildTableData(slaveTable, relationSlaveData, false, null);
                if (!columnDataListResult.isSuccess()) {
                    throw new GeneralException(relationResult.getMsg());
                }
                onlineOperationService.saveNew(slaveTable, columnDataListResult.getData());
            }
        }
    }
}
