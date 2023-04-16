package com.fontana.upmsservice.mapper;

import com.fontana.db.mapper.BaseDaoMapper;
import com.fontana.upmsservice.entity.SysPerm;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 权限资源数据访问操作接口。
 *
 * @author cqf
 * @date 2020-08-08
 */
public interface SysPermMapper extends BaseDaoMapper<SysPerm> {

    /**
     * 获取用户的权限列表。
     *
     * @param userId 用户Id。
     * @return 该用户的权限标识列表。
     */
    List<String> getPermListByUserId(@Param("userId") Long userId);

    /**
     * 查询权限资源地址的用户列表。同时返回详细的分配路径。
     *
     * @param permId    权限资源Id。
     * @param loginName 登录名。
     * @return 包含从权限资源到用户的完整权限分配路径信息的查询结果列表。
     */
    List<Map<String, Object>> getSysUserListWithDetail(
            @Param("permId") Long permId, @Param("loginName") String loginName);

    /**
     * 查询权限资源地址的角色列表。同时返回详细的分配路径。
     *
     * @param permId   权限资源Id。
     * @param roleName 角色名。
     * @return 包含从权限资源到角色的权限分配路径信息的查询结果列表。
     */
    List<Map<String, Object>> getSysRoleListWithDetail(
            @Param("permId") Long permId, @Param("roleName") String roleName);

    /**
     * 查询权限资源地址的菜单列表。同时返回详细的分配路径。
     *
     * @param permId   权限资源Id。
     * @param menuName 菜单名。
     * @return 包含从权限资源到菜单的权限分配路径信息的查询结果列表。
     */
    List<Map<String, Object>> getSysMenuListWithDetail(
            @Param("permId") Long permId, @Param("menuName") String menuName);
}
