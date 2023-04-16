package com.fontana.upmsservice.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fontana.db.object.MyRelationParam;
import com.fontana.db.constant.GlobalDeletedFlag;
import com.fontana.db.mapper.BaseDaoMapper;
import com.fontana.base.result.CallResult;
import com.fontana.db.service.impl.AbstractBaseService;
import com.fontana.db.util.MyModelUtil;
import com.fontana.upmsservice.entity.SysPerm;
import com.fontana.upmsservice.entity.SysPermCodePerm;
import com.fontana.upmsservice.entity.SysPermModule;
import com.fontana.upmsservice.mapper.SysPermCodePermMapper;
import com.fontana.upmsservice.mapper.SysPermMapper;
import com.fontana.upmsservice.service.SysPermModuleService;
import com.fontana.upmsservice.service.SysPermService;
import com.fontana.util.tools.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * 权限资源数据服务类。
 *
 * @author cqf
 * @date 2020-08-08
 */
@Slf4j
@Service("sysPermService")
public class SysPermServiceImpl extends AbstractBaseService<SysPerm, Long> implements SysPermService {

    @Autowired
    private SysPermMapper sysPermMapper;
    @Autowired
    private SysPermCodePermMapper sysPermCodePermMapper;
    @Autowired
    private SysPermModuleService sysPermModuleService;

    /**
     * 返回主对象的Mapper对象。
     *
     * @return 主对象的Mapper对象。
     */
    @Override
    protected BaseDaoMapper<SysPerm> mapper() {
        return sysPermMapper;
    }

    /**
     * 保存新增的权限资源对象。
     *
     * @param perm 新增的权限资源对象。
     * @return 新增后的权限资源对象。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public SysPerm saveNew(SysPerm perm) {
        perm.setPermId(IdUtil.nextLongId());
        MyModelUtil.fillCommonsForInsert(perm);
        perm.setDeletedFlag(GlobalDeletedFlag.NORMAL);
        sysPermMapper.insert(perm);
        return perm;
    }

    /**
     * 更新权限资源对象。
     *
     * @param perm         更新的权限资源对象。
     * @param originalPerm 原有的权限资源对象。
     * @return 更新成功返回true，否则false。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(SysPerm perm, SysPerm originalPerm) {
        MyModelUtil.fillCommonsForUpdate(perm, originalPerm);
        return sysPermMapper.updateById(perm) != 0;
    }

    /**
     * 删除权限资源。
     *
     * @param permId 权限资源主键Id。
     * @return 删除成功返回true，否则false。
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean remove(Long permId) {
        if (sysPermMapper.deleteById(permId) != 1) {
            return false;
        }
        SysPermCodePerm permCodePerm = new SysPermCodePerm();
        permCodePerm.setPermId(permId);
        sysPermCodePermMapper.delete(new QueryWrapper<>(permCodePerm));
        return true;
    }

    /**
     * 获取权限数据列表。
     *
     * @param sysPermFilter 过滤对象。
     * @return 权限列表。
     */
    @Override
    public List<SysPerm> getPermListWithRelation(SysPerm sysPermFilter) {
        QueryWrapper<SysPerm> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc(this.safeMapToColumnName("showOrder"));
        queryWrapper.eq(ObjectUtil.isNotNull(sysPermFilter.getModuleId()),
        this.safeMapToColumnName("moduleId"), sysPermFilter.getModuleId());
        queryWrapper.like(ObjectUtil.isNotNull(sysPermFilter.getUrl()),
        this.safeMapToColumnName("url"), "%" + sysPermFilter.getUrl() + "%");
        List<SysPerm> permList = sysPermMapper.selectList(queryWrapper);
        // 这里因为权限只有字典数据，所以仅仅做字典关联。
        this.buildRelationForDataList(permList, MyRelationParam.dictOnly());
        return permList;
    }

    /**
     * 获取与指定用户关联的权限资源列表，已去重。
     *
     * @param userId 关联的用户主键Id。
     * @return 与指定用户Id关联的权限资源列表。
     */
    @Override
    public Collection<String> getPermListByUserId(Long userId) {
        List<String> urlList = sysPermMapper.getPermListByUserId(userId);
        return new HashSet<>(urlList);
    }

    /**
     * 验证权限资源对象关联的数据是否都合法。
     *
     * @param sysPerm         当前操作的对象。
     * @param originalSysPerm 原有对象。
     * @return 验证结果。
     */
    @Override
    public CallResult verifyRelatedData(SysPerm sysPerm, SysPerm originalSysPerm) {
        if (this.needToVerify(sysPerm, originalSysPerm, SysPerm::getModuleId)) {
            SysPermModule permModule = sysPermModuleService.getById(sysPerm.getModuleId());
            if (permModule == null) {
                return CallResult.error("数据验证失败，关联的权限模块Id并不存在，请刷新后重试！");
            }
        }
        return CallResult.ok();
    }

    /**
     * 查询权限资源地址的用户列表。同时返回详细的分配路径。
     *
     * @param permId    权限资源Id。
     * @param loginName 登录名。
     * @return 包含从权限资源到用户的完整权限分配路径信息的查询结果列表。
     */
    @Override
    public List<Map<String, Object>> getSysUserListWithDetail(Long permId, String loginName) {
        return sysPermMapper.getSysUserListWithDetail(permId, loginName);
    }

    /**
     * 查询权限资源地址的角色列表。同时返回详细的分配路径。
     *
     * @param permId   权限资源Id。
     * @param roleName 角色名。
     * @return 包含从权限资源到角色的权限分配路径信息的查询结果列表。
     */
    @Override
    public List<Map<String, Object>> getSysRoleListWithDetail(Long permId, String roleName) {
        return sysPermMapper.getSysRoleListWithDetail(permId, roleName);
    }

    /**
     * 查询权限资源地址的菜单列表。同时返回详细的分配路径。
     *
     * @param permId   权限资源Id。
     * @param menuName 菜单名。
     * @return 包含从权限资源到菜单的权限分配路径信息的查询结果列表。
     */
    @Override
    public List<Map<String, Object>> getSysMenuListWithDetail(Long permId, String menuName) {
        return sysPermMapper.getSysMenuListWithDetail(permId, menuName);
    }
}
