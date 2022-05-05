package com.fontana.upmsservice.mapper;

import com.fontana.db.mapper.BaseDaoMapper;
import com.fontana.upmsservice.entity.SysDataPermDept;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据权限与部门关系数据访问操作接口。
 *
 * @author cqf
 * @date 2020-08-08
 */
@Mapper
public interface SysDataPermDeptMapper extends BaseDaoMapper<SysDataPermDept> {
}
