package com.fontana.base.object;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Description: 字典类
 * @author: cqf
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DictModel implements Serializable{
	private static final long serialVersionUID = 1L;

	public DictModel() {
	}
	
	public DictModel(String code,String value, String text) {
		this.code = code;
		this.value = value;
		this.text = text;
	}

	/**
	 * 字典code
	 */
	private String code;

	/**
	 * 字典value
	 */
	private String value;
	/**
	 * 字典文本
	 */
	private String text;

}
