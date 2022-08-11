package com.fontana.log;

import com.alibaba.fastjson.JSON;
import com.bluetron.log.controller.ServiceAPIA;
import com.fontana.base.constant.HttpConstants;
import com.fontana.log.controller.ServiceAPIB;
import com.fontana.log.controller.User;
import com.fontana.log.monitor.PointUtil;
import com.fontana.log.producer.producer.BaseLogContent;
import com.fontana.log.producer.producer.BaseLogItem;
import com.fontana.log.producer.producer.RequestLog;
import com.fontana.log.requestlog.config.RequestLogProperties;
import com.fontana.log.requestlog.filter.RequestLogFilter;
import com.fontana.util.date.DateTimeUtil;
import com.fontana.util.request.IpUtil;
import lombok.SneakyThrows;
import org.junit.Before;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@SpringBootApplication(scanBasePackages = "com.*", exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class})

public class logTest {

    @Autowired
    private ServiceAPIA apiA;
    @Autowired
    private ServiceAPIB apiB;

    private MockMvc mockMvc;
    private MockMvc mockMvc2;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Resource
    private RequestLogProperties requestLogProperties;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new RequestLogFilter(requestLogProperties)) //mock请求总是进不来filter，这里强制加过滤器
                .build();
        mockMvc2 = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }



    @Test
    @SneakyThrows
    public void testApiLog() {
        RequestBuilder request = null;

        /*  post请求，content-type为application/json   */
        //构造请求
        request = post("/test/apiA")
                .header(HttpConstants.TENANT_ID_HEADER, "tenant1")
                .header(HttpConstants.FACTORY_ID_HEADER, "factory1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("Xiaoli");//请求体

        //执行请求
        mockMvc2.perform(request)
                .andExpect(status().isOk())//返回HTTP状态为200
                .andExpect(jsonPath("$.msg", is("Hello Xiaoli")))//使用jsonPath解析JSON返回值，判断具体的内容
                .andDo(print()) //打印结果
                .andReturn();////想要返回结果，使用此方法
        assertThat(apiA.methodA("Jack").getMsg()).isEqualTo("Hello Jack");
    }

    @Test
    @SneakyThrows
    public void testAuditLog() {

        RequestBuilder request = null;

        /*  post请求，content-type为application/json   */
        //构造请求
        request = post("/audit/apiB")
                .header(HttpConstants.USER_ID_HEADER, "userId1")
                .header(HttpConstants.USER_NAME_HEADER, "userName1")
                .header(HttpConstants.TENANT_ID_HEADER, "tenant1")
                .header(HttpConstants.FACTORY_ID_HEADER, "factory1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("Xiaoli");//请求体

        //执行请求
        mockMvc2.perform(request)
                .andExpect(status().isOk())//返回HTTP状态为200
                .andExpect(jsonPath("$.msg", is("Hello Xiaoli")))//使用jsonPath解析JSON返回值，判断具体的内容
                .andDo(print()) //打印结果
                .andReturn();////想要返回结果，使用此方法

		/* console print log like:
		14:29:30.337|COMMON|com.fontana.spring.noController.ServiceAPIB|methodB|null|null|null|传入参数为:jack
		*/
        assertThat(apiB.methodB("jack").getMsg()).isEqualTo("Hello jack");

    }

    @Test
    public void testPointLog() {

        //埋点
        PointUtil.info("1", "request-statistics",
                "ip=" + IpUtil.getFirstLocalIpAddress()
                        + "&class=" + getClass().getSimpleName());
        User user = new User();
        user.setName("Anna");
        user.setAge("22");
        user.setSex("female");
        PointUtil.builder().id("2").type("User").properties(user).build();
    }

    @Test
    @SneakyThrows
    public void testSolarLog() {

        //性能日志专用logger
        //requestLog log
        Logger requestLogger = LoggerFactory.getLogger("requestLog");
        requestLogger.info(JSON.toJSONString(buildPerformLog()));
        //由于推送日志是异步执行，这里睡眠一下
        Thread.sleep(1000);
    }

    @Test
    @SneakyThrows
    public void testBaseRequestLog() {
        assertThat(apiA.methodA("Lucy").getMsg()).isEqualTo("Hello Lucy");
        //由于推送日志是异步执行，这里睡眠一下
        Thread.sleep(10000);
    }

    @Test
    @SneakyThrows
    public void testRequestLog(){

        RequestBuilder request = null;

        /*  post请求，content-type为application/json   */
        //构造请求
        request = post("/test/apiA")
                .header(HttpConstants.TENANT_ID_HEADER, "tenant1")
                //.header(HttpConstants.FACTORY_ID_HEADER, "factory1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("Xiaoli");//请求体

        //执行请求
        mockMvc.perform(request)
                .andExpect(status().isOk())//返回HTTP状态为200
                .andExpect(jsonPath("$.msg", is("Hello Xiaoli")))//使用jsonPath解析JSON返回值，判断具体的内容
                .andDo(print()) //打印结果
        .andReturn();////想要返回结果，使用此方法

        /*  post请求，content-type为application/x-www-form-urlencoded   */
        //构造请求
        request = post("/test/apiA")
                .header(HttpConstants.TENANT_ID_HEADER, "tenant1")
                .header(HttpConstants.FACTORY_ID_HEADER, "factory1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .content("Xiaoli");//请求体


        //执行请求
        mockMvc.perform(request)
                .andExpect(status().isOk())//返回HTTP状态为200
                .andExpect(jsonPath("$.msg", is("Hello Xiaoli")))//使用jsonPath解析JSON返回值，判断具体的内容
                .andDo(print()) //打印结果
                .andReturn();////想要返回结果，使用此方法

        /*  GET请求，无content-type   */
        //构造请求
        request = get("/test/apiC?code=123")
                .header(HttpConstants.TENANT_ID_HEADER, "tenant1")
                .header(HttpConstants.FACTORY_ID_HEADER, "factory1")
                .param("name", "Xiaoli");
        //执行请求
        mockMvc.perform(request)
                .andExpect(status().isOk())//返回HTTP状态为200
                .andExpect(jsonPath("$.msg", is("Hello Xiaoli")))//使用jsonPath解析JSON返回值，判断具体的内容
                .andDo(print()) //打印结果
                .andReturn();////想要返回结果，使用此方法

        //由于推送日志是异步执行，这里睡眠一下
        Thread.sleep(10000);

    }




    /**
     * 构建性能日志日志
     *
     * @return
     */
    private RequestLog buildPerformLog() {

        RequestLog log = new RequestLog();
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
            item.pushBack(new BaseLogContent("logTime", DateTimeUtil.formatDate(new Date())));
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
