package com.fontana.JeeOnline.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fontana.cloud.vo.SysPermissionDataRuleVo;
import com.fontana.JeeOnline.entity.OnlAuthData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @interfaceName: OnlAuthDataMapper
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/1/16 16:41
 */
public interface OnlAuthDataMapper extends BaseMapper<OnlAuthData> {
    List<SysPermissionDataRuleVo> queryRoleAuthData(@Param("userId") String var1, @Param("cgformId") String var2);

    List<SysPermissionDataRuleVo> queryDepartAuthData(@Param("userId") String var1, @Param("cgformId") String var2);

    List<SysPermissionDataRuleVo> queryUserAuthData(@Param("userId") String var1, @Param("cgformId") String var2);
}
