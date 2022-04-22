package com.bluetron.nb.common.codegenerate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bluetron.nb.common.codegenerate.entity.Code;
import com.bluetron.nb.common.codegenerate.mapper.CodeMapper;
import com.bluetron.nb.common.codegenerate.service.ICodeService;
import com.bluetron.nb.common.db.constant.GlobalDeletedFlag;
import org.springframework.stereotype.Service;

/**
 * 服务实现类
 *
 * @author Chill
 */
@Service
public class CodeServiceImpl extends ServiceImpl<CodeMapper, Code> implements ICodeService {

	@Override
	public boolean submit(Code code) {
		code.setIsDeleted(GlobalDeletedFlag.NORMAL);
		return saveOrUpdate(code);
	}

}
