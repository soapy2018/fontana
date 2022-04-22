package com.bluetron.nb.common.multitenancy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bluetron.nb.common.base.constant.CommonConstants;
import com.bluetron.nb.common.db.service.impl.SuperServiceImpl;
import com.bluetron.nb.common.multitenancy.annotation.Tenant;
import com.bluetron.nb.common.multitenancy.entity.TenantInfo;
import com.bluetron.nb.common.multitenancy.mapper.TenantInfoMapper;
import com.bluetron.nb.common.multitenancy.service.ITenantInfoService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @className: TenantInfoServiceImpl
 * @Description: 租户信息service
 * @version: v1.0.0
 * @author: cqf
 * @date: 2021/11/4 17:09
 */
//租户信息放在default数据库
@Tenant
@Service
@ConditionalOnProperty(prefix = CommonConstants.TENANT_PREFIX, name = "type", havingValue = "db")
//@ConditionalOnProperty(prefix = CommonConstants.DYNAMIC_DATASOURCE_PREFIX, name = "enabled", havingValue = "true")
public class TenantInfoServiceImpl extends SuperServiceImpl<TenantInfoMapper, TenantInfo> implements ITenantInfoService {
    @Override
    public List<TenantInfo> getActiveTenantsList() {
        return baseMapper.selectList(
                new QueryWrapper<TenantInfo>().eq("is_enable", 1)
        );
    }

    @Override
    //感觉没有太大必要用缓存呢
    //@Cacheable(value = CommonConstants.TENANT_DATASOURCE_KEY, key = "#tenantId", unless = "#result == null")
    public TenantInfo getTenantInfo(String tenantId) {
        return baseMapper.selectOne(
                new QueryWrapper<TenantInfo>().eq("is_enable", 1).eq("tenant_id", tenantId)
        );
    }
}


