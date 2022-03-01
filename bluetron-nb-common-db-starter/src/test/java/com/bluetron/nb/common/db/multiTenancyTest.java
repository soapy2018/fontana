package com.bluetron.nb.common.db;

import com.bluetron.nb.common.base.context.TenantContextHolder;
import com.bluetron.nb.common.db.controller.ServiceAPIA;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @className: multiTenancyTest
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2021/11/9 9:05
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringBootApplication(scanBasePackages = "com.bluetron.nb.common.db")
@Slf4j
public class multiTenancyTest {

    private final MockHttpServletRequest request = new MockHttpServletRequest();
    private final MockHttpServletResponse response = new MockHttpServletResponse();

    private MockMvc mockMvc;

    @Autowired
    private ServiceAPIA apiA;

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void before() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build(); //初始化MockMvc
    }

    @Test
    public void testDBTenancy() {
        log.info("current tenant [{}]", TenantContextHolder.getTenant());
        TenantContextHolder.setTenant("set");
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/methodA")
            )
                    .andExpect(status().isOk()) // 期待返回状态吗码200
                    .andReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDBTenancy2() {
        log.info("current tenant [{}]", TenantContextHolder.getTenant());
        //TenantContextHolder.setTenant("set");
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/findAll")
            )
                    .andExpect(status().isOk()) // 期待返回状态吗码200
                    .andDo(print())
                    .andReturn().getResponse().setCharacterEncoding("UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


