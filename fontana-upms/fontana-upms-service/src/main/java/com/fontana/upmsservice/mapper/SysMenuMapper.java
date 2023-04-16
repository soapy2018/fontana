package com.fontana.upmsservice.mapper;

import com.fontana.db.mapper.BaseDaoMapper;
import com.fontana.upmsservice.entity.SysMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 菜单数据访问操作接口。
 *
 * @author cqf
 * @date 2020-08-08
 */
public interface SysMenuMapper extends BaseDaoMapper<SysMenu> {

    /**
     * 获取登录用户的菜单列表。
     *
     * @param userId 登录用户。
     * @return 菜单列表。
     */
    List<SysMenu> getMenuListByUserId(@Param("userId") Long userId);

    /**
     * 获取当前用户有权访问的在线表单菜单，仅返回类型为BUTTON的菜单。
     *
     * @param userId   指定的用户。
     * @param menuType 菜单类型，NULL则返回全部类型。
     * @return 在线表单关联的菜单列表。
     */
    List<SysMenu> getOnlineMenuListByUserId(
            @Param("userId") Long userId, @Param("menuType") Integer menuType);

    /**
     * 查询菜单的权限资源地址列表。同时返回详细的分配路径。
     *
     * @param menuId 菜单Id。
     * @param url    权限资源地址过滤条件。
     * @return 包含从菜单到权限资源的权限分配路径信息的查询结果列表。
     */
    List<Map<String, Object>> getSysPermListWithDetail(
            @Param("menuId") Long menuId, @Param("url") String url);

    /**
     * 查询菜单的用户列表。同时返回详细的分配路径。
     *
     * @param menuId    菜单Id。
     * @param loginName 登录名。
     * @return 包含从菜单到用户的完整权限分配路径信息的查询结果列表。
     */
    List<Map<String, Object>> getSysUserListWithDetail(
            @Param("menuId") Long menuId, @Param("loginName") String loginName);
}
