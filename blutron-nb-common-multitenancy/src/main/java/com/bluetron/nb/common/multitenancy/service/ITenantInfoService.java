package com.bluetron.nb.common.multitenancy.service;

import com.bluetron.nb.common.db.service.ISuperService;
import com.bluetron.nb.common.multitenancy.entity.TenantInfo;

import java.util.List;

/**
 * @interfaceName: ITenantInfoService
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2021/11/4 17:03
 */
public interface ITenantInfoService extends ISuperService<TenantInfo> {
    List<TenantInfo> getActiveTenantsList();

    TenantInfo getTenantInfo(String tenantId);
}
