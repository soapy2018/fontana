package com.fontana.upmsservice.util;

import cn.hutool.core.collection.CollUtil;
import com.fontana.flowservice.util.BaseFlowDeptPostExtHelper;
import com.fontana.flowservice.util.FlowCustomExtFactory;
import com.fontana.upmsservice.service.SysDeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 为流程提供所需的部门岗位等扩展信息的帮助类。如本部门领导岗位和上级部门领导岗位。
 *
 * @author cqf
 * @date 2021-06-06
 */
@Slf4j
@Component
public class FlowDeptPostExtHelper implements BaseFlowDeptPostExtHelper {

    @Autowired
    private SysDeptService sysDeptService;
    @Autowired
    private FlowCustomExtFactory flowCustomExtFactory;

    @PostConstruct
    public void doRegister() {
        flowCustomExtFactory.registerFlowDeptPostExtHelper(this);
    }

    @Override
    public Long getLeaderDeptPostId(Long deptId) {
        List<Long> deptPostIdList = sysDeptService.getLeaderDeptPostIdList(deptId);
        return CollUtil.isEmpty(deptPostIdList) ? null : deptPostIdList.get(0);
    }

    @Override
    public Long getUpLeaderDeptPostId(Long deptId) {
        List<Long> deptPostIdList = sysDeptService.getUpLeaderDeptPostIdList(deptId);
        return CollUtil.isEmpty(deptPostIdList) ? null : deptPostIdList.get(0);
    }
}
