package com.fontana.sb.exception;

import com.fontana.base.exception.GeneralException;
import com.fontana.base.result.Result;
import com.fontana.base.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringBootApplication
@Slf4j
@ActiveProfiles("local")
public class DefaultExceptionAdviceTest {

    @Autowired
    private DefaultExceptionAdvice handler;

    private MockHttpServletRequest request = new MockHttpServletRequest();
    private MockHttpServletResponse response = new MockHttpServletResponse();


    @Before
    public void before() {
        //LocaleContextHolder.setLocale(Locale.CHINA);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

//    @After
//    public void after() {
//        LocaleContextHolder.setLocale(Locale.getDefault());
//    }


    @Test
    public void handleGeneralExceptionTest() {

        Result errorResult = handler.handleGeneralException(new GeneralException(ResultCode.DATA_IS_WRONG));
        assertThat(errorResult.getCode()).isEqualTo(50002);
        assertThat(errorResult.getMsg()).isEqualTo("数据有误");

        errorResult = handler.handleGeneralException(new GeneralException(CustomerResultCode.CUSTOMER_DEFINE_ERROR));
        assertThat(errorResult.getCode()).isEqualTo(90001);
        assertThat(errorResult.getMsg()).isEqualTo("自定义系统错误");

        errorResult = handler.handleGeneralException(new GeneralException("我自己定义的错误"));
        assertThat(errorResult.getCode()).isEqualTo(-1);
        assertThat(errorResult.getMsg()).isEqualTo("我自己定义的错误");
    }

    @Test
    public void handleExceptionTest() {

        Result errorResult = handler.handleException(new Exception());
        assertThat(errorResult.getCode()).isEqualTo(-1);
        assertThat(errorResult.getMsg()).isEqualTo("未知异常");
    }

    @Test
    public void handleSQLExceptionTest() {

        Result errorResult = handler.handleSQLException(new SQLException());
        assertThat(errorResult.getCode()).isEqualTo(-1);
        assertThat(errorResult.getMsg()).isEqualTo("服务运行SQLException异常");
    }

}
