package com.bluetron.nb.common.sb;

import com.bluetron.nb.common.base.result.Result;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @className: ResultTest
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2021/9/27 16:56
 */
public class ResultTest {
    @Test
    public void testSucceed() {
        assertThat(Result.succeed().getCode()).isEqualTo(1);
        assertThat(Result.succeed().getMsg()).isEqualTo("成功");
    }
}


