package com.bluetron.nb.common.sb.noController;

import com.bluetron.nb.common.sb.log.AuditLog;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AuditLog(operation = "'传入参数为:' + #name")
public class ServiceAPIB {

    public String methodB(String name) {
        return "hello" + name;
    }

    public String methodC(String tool) {
        return "hello" + tool;
    }

}
