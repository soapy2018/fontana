package com.fontana.demo.xxljob;

import com.fontana.xxljob.annotation.XxlRegister;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Service;

/**
 * @author chenqingfeng
 * @description xxljob测试服务
 * @date 2022/10/19 10:34
 */
@Service
public class TestService {

    @XxlJob(value = "testJob")
    @XxlRegister(cron = "0 0 0 * * ? *",
            author = "cqf",
            jobDesc = "测试job")
    public void testJob(){
        System.out.println("#公众号：码农参上");
    }


    @XxlJob(value = "testJob222")
    @XxlRegister(cron = "59 1-2 0 * * ?",
            triggerStatus = 1)
    public void testJob2(){
        System.out.println("#作者：cqf");
    }

    @XxlJob(value = "testJob444")
    @XxlRegister(cron = "59 59 23 * * ?")
    public void testJob4(){
        System.out.println("hello xxl job");
    }
}