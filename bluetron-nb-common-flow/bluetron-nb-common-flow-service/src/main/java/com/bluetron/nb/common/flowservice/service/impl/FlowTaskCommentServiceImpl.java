package com.bluetron.nb.common.flowservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bluetron.nb.common.base.object.TokenData;
import com.bluetron.nb.common.db.mapper.BaseDaoMapper;
import com.bluetron.nb.common.db.service.impl.ABaseService;
import com.bluetron.nb.common.flowservice.entity.FlowTaskComment;
import com.bluetron.nb.common.flowservice.mapper.FlowTaskCommentMapper;
import com.bluetron.nb.common.util.request.WebContextUtil;
import com.bluetron.nb.common.util.tools.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 流程任务批注数据操作服务类。
 *
 * @author cqf
 * @date 2021-06-06
 */
@Slf4j
@Service("flowTaskCommentService")
public class FlowTaskCommentServiceImpl extends ABaseService<FlowTaskComment, Long> implements com.bluetron.nb.common.flowservice.service.FlowTaskCommentService {

    @Autowired
    private FlowTaskCommentMapper flowTaskCommentMapper;

    /**
     * 返回当前Service的主表Mapper对象。
     *
     * @return 主表Mapper对象。
     */
    @Override
    protected BaseDaoMapper<FlowTaskComment> mapper() {
        return flowTaskCommentMapper;
    }

    /**
     * 保存新增对象。
     *
     * @param flowTaskComment 新增对象。
     * @return 返回新增对象。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public FlowTaskComment saveNew(FlowTaskComment flowTaskComment) {
        flowTaskComment.setId(IdUtil.nextLongId());
        TokenData tokenData = WebContextUtil.takeTokenFromRequest();
        flowTaskComment.setCreateUserId(tokenData.getUserId());
        flowTaskComment.setCreateUsername(tokenData.getShowName());
        flowTaskComment.setCreateTime(new Date());
        flowTaskCommentMapper.insert(flowTaskComment);
        return flowTaskComment;
    }

    /**
     * 查询指定流程实例Id下的所有审批任务的批注。
     *
     * @param processInstanceId 流程实例Id。
     * @return 查询结果集。
     */
    @Override
    public List<FlowTaskComment> getFlowTaskCommentList(String processInstanceId) {
        LambdaQueryWrapper<FlowTaskComment> queryWrapper =
                new LambdaQueryWrapper<FlowTaskComment>().eq(FlowTaskComment::getProcessInstanceId, processInstanceId);
        queryWrapper.orderByAsc(FlowTaskComment::getId);
        return flowTaskCommentMapper.selectList(queryWrapper);
    }
}
