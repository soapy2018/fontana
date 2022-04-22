package com.bluetron.nb.common.multitenancy.service.impl;

import com.bluetron.nb.common.db.service.impl.SuperServiceImpl;
import com.bluetron.nb.common.multitenancy.mapper.SysUserMapper;
import com.bluetron.nb.common.multitenancy.model.SysUser;
import com.bluetron.nb.common.multitenancy.service.SysUserService;
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