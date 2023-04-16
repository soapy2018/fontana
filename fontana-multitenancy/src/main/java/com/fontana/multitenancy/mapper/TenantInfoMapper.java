package com.fontana.multitenancy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fontana.multitenancy.entity.TenantInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @interfaceName: TenantInfoMapper
 * @Description: mapper
 * @version: v1.0.0
 * @author: cqf
 * @date: 2021/11/4 17:08
 */
@Mapper
public interface TenantInfoMapper extends BaseMapper<TenantInfo> {

}
