package com.bluetron.nb.common.util.lang;

import com.google.common.collect.Lists;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *   字符串 utils 测试类
 * @author wuwenli
 *
 */
public class StringUtilsTest {

	@Test
	public void testStrNullOrEmpty() {
		assertThat( StringUtils.isBlank(null) ).isEqualTo(true); // null 为 true
		assertThat( StringUtils.isBlank("") ).isEqualTo(true); // "" 为 true
		assertThat( StringUtils.isBlank("     ") ).isEqualTo(true); // "    " 为 true
	}
	
	@Test
	public void testListToString() {
	    
	    assertThat(
                StringUtils.listToString(null)).isEqualTo("");
	    
	    assertThat(
	            StringUtils.listToString(Lists.newArrayList("1","2",null))).isEqualTo("1,2");
	    
	    assertThat(
                StringUtils.listToString(Lists.newArrayList())).isEqualTo("");
	    
	    assertThat(
                StringUtils.listToString(Lists.newArrayList("aaa","bbb","ccc"))).isEqualTo("aaa,bbb,ccc");
	    
	}
	
}
