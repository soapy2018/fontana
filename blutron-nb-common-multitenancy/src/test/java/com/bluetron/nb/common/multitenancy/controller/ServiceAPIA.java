package com.bluetron.nb.common.multitenancy.controller;

import com.bluetron.nb.common.base.context.TenantContextHolder;
import com.bluetron.nb.common.multitenancy.annotation.Tenant;
import com.bluetron.nb.common.multitenancy.service.ITenantInfoService;
import com.bluetron.nb.common.multitenancy.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ServiceAPIA {
    @Autowired
    ITenantInfoService tenantInfoService;

    @Autowired
    SysUserService sysUserService;

    @GetMapping(value = "/methodA")
    public String methodA(String name) {
        tenantInfoService.getActiveTenantsList();
        log.info("current tenant is [{}]", TenantContextHolder.getTenant());
        return "hello" + name;
    }

    @Tenant(value = "tenantID1")
    @GetMapping(value = "/findAll")
    public Object findAll() {
        return sysUserService.findAll();
    }

    @Tenant(value = "tenantID2")
    @GetMapping(value = "/findAll2")
    public Object findAll2() {
        return sysUserService.findAll();
    }

}
