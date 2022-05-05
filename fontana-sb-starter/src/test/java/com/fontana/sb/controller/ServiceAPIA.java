package com.fontana.sb.controller;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceAPIA {
    public String methodA(String name) {
        return "hello" + name;
    }

}
