package com.fontana.upmsservice.mapper;

import com.fontana.db.mapper.BaseDaoMapper;
import com.fontana.upmsservice.entity.SysPermModule;

import java.util.List;

/**
 * 权限资源模块数据访问操作接口。
 *
 * @author cqf
 * @date 2020-08-08
 */
public interface SysPermModuleMapper extends BaseDaoMapper<SysPermModule> {

    /**
     * 获取整个权限模块和权限关联后的全部数据。
     *
     * @return 关联的权限模块和权限资源列表。
     */
    List<SysPermModule> getPermModuleAndPermList();
}
