package com.fontana.sb;

import com.fontana.sb.i18n.I18nMessageResourceAccessor;
import com.fontana.util.tools.SpringContextHolder;
import lombok.Data;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@SpringBootApplication
@ActiveProfiles("dev")
public class SpringConfigTest {

    @Autowired
    private I18nMessageResourceAccessor accessor;

    @Autowired
    ThreadPoolTaskExecutor poolExecutor;

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

    @Test
    public void testGetMessage() {
        //三个配置文件，最先匹配的是core
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
    public void testAsynTask() throws InterruptedException {
        //一定要休眠 不然主线程关闭了，子线程还没有启动
        poolExecutor.submit(new Thread(()->
            System.out.println("threadPoolTaskExecutor 创建线程")
        ));
        Thread.sleep(1000);
    }
}

@Data
class TestBean {
    private String name;
}
