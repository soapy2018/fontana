package com.bluetron.log.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/test")
public class ServiceAPIA {
    public String methodA(String name) {
        log.info("Hello " + name);
        return "hello" + name;
    }

}
