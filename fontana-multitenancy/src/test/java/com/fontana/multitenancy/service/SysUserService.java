package com.fontana.multitenancy.service;

import com.fontana.db.service.ISuperService;
import com.fontana.multitenancy.model.SysUser;

import java.util.List;

public interface SysUserService extends ISuperService<SysUser> {

    /**
     * 查找所有用户
     *
     * @return
     */
    List<SysUser> findAll();

}