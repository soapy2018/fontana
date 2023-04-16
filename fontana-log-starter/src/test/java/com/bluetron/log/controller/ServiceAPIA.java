package com.bluetron.log.controller;

import com.fontana.base.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/test")
public class ServiceAPIA {
    @PostMapping("/apiA")
    public Result methodA(@RequestBody String name) {
        log.info("Hello " + name);
        return Result.succeed("Hello " + name);
    }

    @GetMapping("/apiC")
    public Result methodC(String name) {
        log.info("Hello " + name);
        return Result.succeed("Hello " + name);
    }

}
