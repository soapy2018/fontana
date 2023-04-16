package com.fontana.db.support;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 分页工具
 *
 * @author cqf
 */
@Data
@Accessors(chain = true)
public class Query {

	/**
	 * 当前页
	 */
	private Integer current;

	/**
	 * 每页的数量
	 */
	private Integer size;

	/**
	 * 升序的字段名列表，","分隔
	 */
	private String ascs;

	/**
	 * 降序的字段名列表，","分隔
	 */
	private String descs;

}
