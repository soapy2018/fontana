package com.bluetron.nb.common.codegenerate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bluetron.nb.common.codegenerate.entity.Code;

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
