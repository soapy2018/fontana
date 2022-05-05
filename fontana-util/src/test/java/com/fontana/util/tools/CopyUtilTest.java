package com.fontana.util.tools;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CopyUtilTest {

    @Test
    public void test() {

        List<String> list = Lists.newArrayList("1", "2", "3");

        List<String> newlist = CopyUtil.deepCopy(list);

        newlist.add("4");

        System.out.println("原list值：" + list);
        System.out.println("新list值：" + newlist);

        assertThat(list.size()).isEqualTo(3);
        assertThat(newlist.size()).isEqualTo(4);

    }

}
