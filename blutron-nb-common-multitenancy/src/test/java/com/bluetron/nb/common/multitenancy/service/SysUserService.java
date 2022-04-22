package com.bluetron.nb.common.multitenancy.service;

import com.bluetron.nb.common.db.service.ISuperService;
import com.bluetron.nb.common.multitenancy.model.SysUser;

import java.util.List;

public interface SysUserService extends ISuperService<SysUser> {

    /**
     * 查找所有用户
     *
     * @return
     */
    List<SysUser> findAll();

}