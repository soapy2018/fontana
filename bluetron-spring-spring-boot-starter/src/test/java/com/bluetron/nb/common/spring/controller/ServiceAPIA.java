package com.bluetron.nb.common.spring.controller;

import com.bluetron.nb.common.spring.log.AuditLog;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceAPIA {
    public String methodA(String name) {
        return "hello" + name;
    }

}
