package com.bluetron.nb.common.upmsservice.mapper;

import com.bluetron.nb.common.db.mapper.BaseDaoMapper;
import com.bluetron.nb.common.upmsservice.entity.SysPost;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 岗位管理数据操作访问接口。
 *
 * @author cqf
 * @date 2021-06-06
 */
public interface SysPostMapper extends BaseDaoMapper<SysPost> {

    /**
     * 获取过滤后的对象列表。
     *
     * @param sysPostFilter 主表过滤对象。
     * @param orderBy       排序字符串，order by从句的参数。
     * @return 对象列表。
     */
    List<SysPost> getSysPostList(
            @Param("sysPostFilter") SysPost sysPostFilter, @Param("orderBy") String orderBy);

    /**
     * 获取指定部门的岗位列表。
     *
     * @param deptId        部门Id。
     * @param sysPostFilter 从表过滤对象。
     * @param orderBy       排序字符串，order by从句的参数。
     * @return 岗位数据列表。
     */
    List<SysPost> getSysPostListByDeptId(
            @Param("deptId") Long deptId,
            @Param("sysPostFilter") SysPost sysPostFilter,
            @Param("orderBy") String orderBy);

    /**
     * 根据关联主表Id，获取关联从表中没有和主表建立关联关系的数据列表。
     *
     * @param deptId        关联主表Id。
     * @param sysPostFilter 过滤对象。
     * @param orderBy       排序字符串，order by从句的参数。
     * @return 与主表没有建立关联的从表数据列表。
     */
    List<SysPost> getNotInSysPostListByDeptId(
            @Param("deptId") Long deptId,
            @Param("sysPostFilter") SysPost sysPostFilter,
            @Param("orderBy") String orderBy);
}