package com.fontana.codegenerate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fontana.codegenerate.entity.Code;
import com.fontana.codegenerate.mapper.CodeMapper;
import com.fontana.codegenerate.service.ICodeService;
import com.fontana.db.constant.GlobalDeletedFlag;
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
