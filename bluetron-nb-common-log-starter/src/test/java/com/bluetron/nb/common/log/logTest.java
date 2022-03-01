package com.bluetron.nb.common.log;

import com.bluetron.nb.common.log.controller.ServiceAPIA;
import com.bluetron.nb.common.log.noController.ServiceAPIB;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@SpringBootApplication(scanBasePackages = "com.bluetron.nb.common.log")
public class logTest {

    @Autowired
    private ServiceAPIA apiA;
    @Autowired
    private ServiceAPIB apiB;


    @Test
    public void testApiLog() {
		/* console print log like:
		2021-09-28 20:18:14.028  INFO 65544 --- [           main] c.b.nb.common.spring.log.ApiLogConfig    :  api : http://localhost
		2021-09-28 20:18:14.031  INFO 65544 --- [           main] c.b.nb.common.spring.log.ApiLogConfig    : Class Method : com.bluetron.nb.common.spring.controller.ServiceAPI.methodA
		2021-09-28 20:18:14.031  INFO 65544 --- [           main] c.b.nb.common.spring.log.ApiLogConfig    : the args of this api: [jack]
		2021-09-28 20:18:14.037  INFO 65544 --- [           main] c.b.nb.common.spring.log.ApiLogConfig    : Time-Consuming : 10 ms
		*/
        assertThat(apiA.methodA("jack")).isEqualTo("hellojack");
    }

    @Test
    public void testAuditLog() {
		/* console print log like:
		14:29:30.337|COMMON|com.bluetron.nb.common.spring.noController.ServiceAPIB|methodB|null|null|null|传入参数为:jack
		*/
        assertThat(apiB.methodB("jack")).isEqualTo("hellojack");

        assertThat(apiB.methodC("car")).isEqualTo("hellocar");
    }
}
