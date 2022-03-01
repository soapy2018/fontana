package com.bluetron.nb.common.db.query;

import lombok.Data;

import java.io.Serializable;

@Data
public class QueryCondition implements Serializable {

	private static final long serialVersionUID = 4740166316629191651L;
	
	private String field;
	/** 组件的类型（例如：input、select、radio） */
	private String type;
	/**
	 * 对应的数据库字段的类型
	 * 支持：int、bigDecimal、short、long、float、double、boolean
	 */
	private String dbType;
	private String rule;
	private String val;

}
