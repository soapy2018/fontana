package com.fontana.flowservice.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fontana.db.mapper.BaseDaoMapper;
import com.fontana.db.service.impl.AbstractBaseService;
import com.fontana.flowservice.entity.FlowTaskExt;
import com.fontana.flowservice.mapper.FlowTaskExtMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 流程任务扩展数据操作服务类。
 *
 * @author cqf
 * @date 2021-06-06
 */
@Slf4j
@Service("flowTaskExtService")
public class FlowTaskExtServiceImpl extends AbstractBaseService<FlowTaskExt, String> implements com.fontana.flowservice.service.FlowTaskExtService {

    @Autowired
    private FlowTaskExtMapper flowTaskExtMapper;

    /**
     * 返回当前Service的主表Mapper对象。
     *
     * @return 主表Mapper对象。
     */
    @Override
    protected BaseDaoMapper<FlowTaskExt> mapper() {
        return flowTaskExtMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveBatch(List<FlowTaskExt> flowTaskExtList) {
        if (CollUtil.isNotEmpty(flowTaskExtList)) {
            flowTaskExtMapper.insertList(flowTaskExtList);
        }
    }

    @Override
    public FlowTaskExt getByProcessDefinitionIdAndTaskId(String processDefinitionId, String taskId) {
        FlowTaskExt filter = new FlowTaskExt();
        filter.setProcessDefinitionId(processDefinitionId);
        filter.setTaskId(taskId);
        return flowTaskExtMapper.selectOne(new QueryWrapper<>(filter));
    }

    @Override
    public List<FlowTaskExt> getByProcessDefinitionId(String processDefinitionId) {
        FlowTaskExt filter = new FlowTaskExt();
        filter.setProcessDefinitionId(processDefinitionId);
        return flowTaskExtMapper.selectList(new QueryWrapper<>(filter));
    }
}
