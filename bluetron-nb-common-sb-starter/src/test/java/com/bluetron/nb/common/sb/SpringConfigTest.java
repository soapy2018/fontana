package com.bluetron.nb.common.sb;

import com.bluetron.nb.common.sb.context.SpringContextHolder;
import com.bluetron.nb.common.sb.controller.ServiceAPIA;
import com.bluetron.nb.common.sb.i18n.I18nMessageResourceAccessor;
import com.bluetron.nb.common.sb.noController.ServiceAPIB;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@SpringBootApplication(scanBasePackages = "com.bluetron", exclude = DataSourceAutoConfiguration.class)
@ActiveProfiles("dev")
public class SpringConfigTest {

    @Autowired
    private I18nMessageResourceAccessor accessor;
    @Autowired
    private ServiceAPIA apiA;
    @Autowired
    private ServiceAPIB apiB;

    @Test
    public void testSpringContextHolder() {
        Assert.assertEquals("dev", SpringContextHolder.getActiveProfile());
    }

    @Test
    public void testGetMessage() {
        //三个配置文件，最先匹配的市core
        assertThat(accessor.getMessage("success", "zh_CN")).isEqualTo("成功啦");
        assertThat(accessor.getMessage("success", "en_US")).isEqualTo("success");
        assertThat(accessor.getMessage("xxxx", "en_US")).isEqualTo("");
        //若不指定Local，默认Local是zh_CN，
        assertThat(accessor.getMessage("success")).isEqualTo("成功啦");
        assertThat(accessor.getMessage("supos.token.expire")).isEqualTo("SupOS token 失效");
        // new testcase by default
        assertThat(accessor.getMessage("abc", Locale.CHINA, "abcd")).isEqualTo("abcd");
    }

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
