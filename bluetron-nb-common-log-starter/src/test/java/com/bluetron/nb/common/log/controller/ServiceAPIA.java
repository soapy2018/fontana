package com.bluetron.nb.common.log.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ServiceAPIA {
    public String methodA(String name) {
        log.info("Hello " + name);
        return "hello" + name;
    }

}
