package com.bluetron.nb.common.sb.noController;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceAPIB {

    public String methodB(String name) {
        return "hello" + name;
    }

    public String methodC(String tool) {
        return "hello" + tool;
    }

}
