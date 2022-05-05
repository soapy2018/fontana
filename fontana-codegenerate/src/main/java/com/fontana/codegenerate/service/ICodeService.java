package com.fontana.codegenerate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fontana.codegenerate.entity.Code;

/**
 * 服务类
 *
 * @author Chill
 */
public interface ICodeService extends IService<Code> {

	/**
	 * 提交
	 *
	 * @param code
	 * @return
	 */
	boolean submit(Code code);

}
