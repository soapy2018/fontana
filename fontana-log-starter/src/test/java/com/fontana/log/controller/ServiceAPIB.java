package com.fontana.log.controller;

import com.fontana.log.auditLog.AuditLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AuditLog(operation = "'查询人名:' + #name")
public class ServiceAPIB {

    public String methodB(String name) {
        log.info("Hello " + name);
        return "hello" + name;
    }

    public String methodC(String tool) {
        log.info("Hello " + tool);
        return "hello" + tool;
    }

}
