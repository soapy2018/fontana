package com.bluetron.nb.common.log.noController;

import com.bluetron.nb.common.log.auditLog.AuditLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AuditLog(operation = "'传入参数为:' + #name")
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
