package com.fontana.log;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.alibaba.fastjson.JSON;
import com.bluetron.common.log.producer.producer.BaseLogContent;
import com.bluetron.common.log.producer.producer.BaseLogItem;
import com.bluetron.common.log.producer.producer.PerformanceLog;
import com.bluetron.common.log.producer.util.LocalDateTimeUtil;
import com.bluetron.log.controller.ServiceAPIA;
import com.fontana.log.controller.ServiceAPIB;
import com.fontana.log.monitor.PointUtil;
import com.fontana.util.request.IpUtil;
import com.fontana.util.request.WebContextUtil;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@SpringBootApplication(scanBasePackages = "com.*", exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,
        DruidDataSourceAutoConfigure.class})
public class logTest {

    @Autowired
    private ServiceAPIA apiA;
    @Autowired
    private ServiceAPIB apiB;


    @Test
    public void testApiLog() {
        assertThat(apiA.methodA("Jack")).isEqualTo("helloJack");
    }

    @Test
    public void testAuditLog() {
		/* console print log like:
		14:29:30.337|COMMON|com.fontana.spring.noController.ServiceAPIB|methodB|null|null|null|传入参数为:jack
		*/
        assertThat(apiB.methodB("jack")).isEqualTo("hellojack");

        assertThat(apiB.methodC("car")).isEqualTo("hellocar");
    }

    @Test
    public void testPointLog() {
        //埋点
        PointUtil.info("1", "request-statistics",
                "ip=" + IpUtil.getFirstLocalIpAddress());
    }

    @Test
    @SneakyThrows
    public void testSolarLog() {

        //性能日志专用logger
        //PerformanceLog log
        Logger performanceLogger = LoggerFactory.getLogger("performanceLog");
        performanceLogger.info(JSON.toJSONString(buildPerformLog()));
        //由于推送日志是异步执行，这里睡眠一下
        Thread.sleep(1000);
    }

    @Test
    @SneakyThrows
    public void testPerformanceLog() {
        assertThat(apiA.methodA("Lucy")).isEqualTo("helloLucy");
        //由于推送日志是异步执行，这里睡眠一下
        Thread.sleep(1000);
    }



    /**
     * 构建性能日志日志
     *
     * @return
     */
    private PerformanceLog buildPerformLog() {

        PerformanceLog log = new PerformanceLog();
        log.setHeader("test head");
        log.setPath("test path");
        log.setMethod("test");
        log.setRequestTime(System.currentTimeMillis());
        log.setRemoteIP(IpUtil.getFirstLocalIpAddress());
        log.setTenantId("test tenant");
        log.setFactoryId("test factory");
        log.setUserId("test user");
        log.setUserName("test user");
        log.setParameter("testParameter='test");
        return log;
    }


    /**
     * 构建批量日志
     *
     * @param number
     * @return
     */
    private List<BaseLogItem> buildLogItem(int number) {
        if (number <= 0) {
            number = 1;
        }
        List<BaseLogItem> arrayList = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            BaseLogItem item = new BaseLogItem();
            item.pushBack(new BaseLogContent("level", "INFO"));
            item.pushBack(new BaseLogContent("logTime", LocalDateTimeUtil.format(new Date())));
            item.pushBack(new BaseLogContent("thread", "main"));
            item.pushBack(new BaseLogContent("location",
                    "com.bluetron.app.middle.log.producer.LogbackTest.infoTest(LogbackTest.java:24)"));
            item.pushBack(new BaseLogContent("host", "192.168.31.179"));
            item.pushBack(new BaseLogContent("hostName", "Mac-Studio.local"));
            item.pushBack(new BaseLogContent("message", "日志单元测试  " + System.currentTimeMillis()));
            arrayList.add(item);
        }
        return arrayList;
    }



}
