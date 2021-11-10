package com.bluetron.nb.common.db.service.impl;

import com.bluetron.nb.common.db.mapper.SysUserMapper;
import com.bluetron.nb.common.db.model.SysUser;
import com.bluetron.nb.common.db.service.ISuperService;
import com.bluetron.nb.common.db.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SysUserServiceImpl extends SuperServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    
    @Autowired
    private SysUserMapper sysUserMapper;
    
    @Override
    public List<SysUser> findAll() {
        return baseMapper.selectList(null);
    }
}