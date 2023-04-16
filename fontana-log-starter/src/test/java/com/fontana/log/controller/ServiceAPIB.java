package com.fontana.log.controller;

import com.fontana.base.result.Result;
import com.fontana.log.auditlog.AuditLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/audit")
@AuditLog(operation = "'查询人名:' + #name")
public class ServiceAPIB {

    @PostMapping("/apiB")
    public Result methodB(@RequestBody String name) {
        log.info("Hello " + name);
        return Result.succeed("Hello " + name);
    }
}
