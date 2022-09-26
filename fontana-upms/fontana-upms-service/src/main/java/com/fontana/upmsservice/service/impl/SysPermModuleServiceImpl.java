package com.fontana.upmsservice.service.impl;

import com.fontana.db.constant.GlobalDeletedFlag;
import com.fontana.db.mapper.BaseDaoMapper;
import com.fontana.db.service.impl.AbsBaseService;
import com.fontana.db.util.MyModelUtil;
import com.fontana.upmsservice.entity.SysPerm;
import com.fontana.upmsservice.entity.SysPermModule;
import com.fontana.upmsservice.mapper.SysPermModuleMapper;
import com.fontana.upmsservice.service.SysPermModuleService;
import com.fontana.upmsservice.service.SysPermService;
import com.fontana.util.tools.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 权限资源模块数据服务类。
 *
 * @author cqf
 * @date 2020-08-08
 */
@Slf4j
@Service("sysPermModuleService")
public class SysPermModuleServiceImpl extends AbsBaseService<SysPermModule, Long> implements SysPermModuleService {

    @Autowired
    private SysPermModuleMapper sysPermModuleMapper;
    @Autowired
    private SysPermService sysPermService;

    /**
     * 返回主对象的Mapper对象。
     *
     * @return 主对象的Mapper对象。
     */
    @Override
    protected BaseDaoMapper<SysPermModule> mapper() {
        return sysPermModuleMapper;
    }

    /**
     * 保存新增的权限资源模块对象。
     *
     * @param sysPermModule 新增的权限资源模块对象。
     * @return 新增后的权限资源模块对象。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysPermModule saveNew(SysPermModule sysPermModule) {
        sysPermModule.setModuleId(IdUtil.nextLongId());
        MyModelUtil.fillCommonsForInsert(sysPermModule);
        sysPermModule.setDeletedFlag(GlobalDeletedFlag.NORMAL);
        sysPermModuleMapper.insert(sysPermModule);
        return sysPermModule;
    }

    /**
     * 更新权限资源模块对象。
     *
     * @param sysPermModule         更新的权限资源模块对象。
     * @param originalSysPermModule 原有的权限资源模块对象。
     * @return 更新成功返回true，否则false
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(SysPermModule sysPermModule, SysPermModule originalSysPermModule) {
        MyModelUtil.fillCommonsForUpdate(sysPermModule, originalSysPermModule);
        return sysPermModuleMapper.updateById(sysPermModule) != 0;
    }

    /**
     * 删除指定的权限资源模块。
     *
     * @param moduleId 权限资源模块主键Id。
     * @return 删除成功返回true，否则false。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean remove(Long moduleId) {
        return sysPermModuleMapper.deleteById(moduleId) == 1;
    }

    /**
     * 获取权限模块资源及其关联的权限资源列表。
     *
     * @return 权限资源模块及其关联的权限资源列表。
     */
    @Override
    public List<SysPermModule> getPermModuleAndPermList() {
        return sysPermModuleMapper.getPermModuleAndPermList();
    }

    /**
     * 判断是否存在下级权限资源模块。
     *
     * @param moduleId 权限资源模块主键Id。
     * @return 存在返回true，否则false。
     */
    @Override
    public boolean hasChildren(Long moduleId) {
        SysPermModule permModule = new SysPermModule();
        permModule.setParentId(moduleId);
        return this.getCountByFilter(permModule) > 0;
    }

    /**
     * 判断是否存在权限数据。
     *
     * @param moduleId 权限资源模块主键Id。
     * @return 存在返回true，否则false。
     */
    @Override
    public boolean hasModulePerms(Long moduleId) {
        SysPerm filter = new SysPerm();
        filter.setModuleId(moduleId);
        return sysPermService.getCountByFilter(filter) > 0;
    }
}
