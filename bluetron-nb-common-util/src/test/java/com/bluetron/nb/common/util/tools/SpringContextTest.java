package com.bluetron.nb.common.util.tools;

import com.bluetron.nb.common.util.tools.example.TestBean;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringBootApplication
public class SpringContextTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void before() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build(); //初始化MockMvc
    }


    @Test
    public void testSpringContextHolder() {
        Assert.assertEquals("dev", SpringContextHolder.getActiveProfile());
        SpringContextHolder.createBean(TestBean.class);
        SpringContextHolder.createBean(TestBean.class);
        TestBean bean1 = SpringContextHolder.getBean("TestBean");
        TestBean bean2 = SpringContextHolder.getBean(TestBean.class);
        bean1.setName("lili");
        bean2.setName("lucy");
        System.err.println(bean1);
        System.err.println(bean2);
    }

}
