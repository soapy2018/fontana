package com.fontana.multitenancy.service.impl;

import com.fontana.db.service.impl.SuperServiceImpl;
import com.fontana.multitenancy.mapper.SysUserMapper;
import com.fontana.multitenancy.model.SysUser;
import com.fontana.multitenancy.service.SysUserService;
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