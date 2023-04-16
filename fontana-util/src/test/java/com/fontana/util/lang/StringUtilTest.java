package com.fontana.util.lang;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 字符串 util 测试类
 *
 * @author wuwenli
 */
public class StringUtilTest {

    @Test
    public void testStrNullOrEmpty() {
        assertThat(StringUtil.isBlank(null)).isEqualTo(true); // null 为 true
        assertThat(StringUtil.isBlank("")).isEqualTo(true); // "" 为 true
        assertThat(StringUtil.isBlank("     ")).isEqualTo(true); // "    " 为 true
    }

    @Test
    public void testJoinIgnoreNull() {

        assertThat(
                StringUtil.joinIgnoreNull(null)).isEqualTo("");

        assertThat(
                StringUtil.joinIgnoreNull(Lists.newArrayList("1", "2", null))).isEqualTo("1,2");

        assertThat(
                StringUtil.joinIgnoreNull(Lists.newArrayList())).isEqualTo("");

        assertThat(
                StringUtil.joinIgnoreNull(Lists.newArrayList("aaa", "bbb", "ccc"))).isEqualTo("aaa,bbb,ccc");


        assertThat(
                StringUtil.join(Lists.newArrayList("1", "2"), ",")).isEqualTo("1,2");


        assertThat(
                StringUtil.join(Sets.newHashSet(1, 2), ",")).isEqualTo("1,2");

        assertThat(
                StringUtil.joinIgnoreNull(Sets.newHashSet("1", "2", "1", null), ",")).isEqualTo("1,2");

        assertThat(
                StringUtil.join(Lists.newArrayList("1", "2", null), ",")).isEqualTo("1,2,");

        assertThat(
                StringUtil.join(Sets.newHashSet("1", "2", "1"), ",")).isEqualTo("1,2");

        Long[] numLong = {1L, 2L, 3L};
        Integer[] numInt = {0, 1, 2};
        String[] str = {"S", "T"};

        assertThat(
                StringUtil.join(numLong, ',')).isEqualTo("1,2,3");

        assertThat(
                StringUtil.join(numInt, ',')).isEqualTo("0,1,2");

        //assertThat(str).isEqualTo("S,T");

        assertThat(
                StringUtil.join(str, ',')).isEqualTo("S,T");

        assertThat(
                StringUtil.getLongArrayFromString("1,2", ",")[0]).isEqualTo(1);
        assertThat(
                StringUtil.getLongArrayFromString("1,2", ",")[1]).isEqualTo(2);

        assertThat(
                StringUtil.getLongArrayFromString("1,2")[0]).isEqualTo(1);
        assertThat(
                StringUtil.getLongArrayFromString("1,2")[1]).isEqualTo(2);

        assertThat(
                StringUtil.getLongListFromString("1,2", ",").get(0)).isEqualTo(1);
        assertThat(
                StringUtil.getLongListFromString("1,2", ",").get(1)).isEqualTo(2);

        assertThat(
                StringUtil.getLongSetFromString("1,2", ",").contains(1));
        assertThat(
                StringUtil.getLongSetFromString("1,2,1,2,1", ",").size()).isEqualTo(2);

        assertThat(
                StringUtil.getLongSetFromString("1;2", ";").contains(1));
        assertThat(
                StringUtil.getLongSetFromString("1;2;1;2;1", ";").size()).isEqualTo(2);

    }

}
