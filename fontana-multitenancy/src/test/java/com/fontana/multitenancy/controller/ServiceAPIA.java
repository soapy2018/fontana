package com.fontana.multitenancy.controller;

import com.fontana.base.context.TenantContextHolder;
import com.fontana.multitenancy.annotation.Tenant;
import com.fontana.multitenancy.service.ITenantInfoService;
import com.fontana.multitenancy.service.SysUserService;
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
