package com.bluetron.nb.common.db.service;

import com.bluetron.nb.common.db.model.SysUser;
import java.util.List;

public interface SysUserService extends ISuperService<SysUser> {

    /**
     * 查找所有用户
     * @return
     */
    List<SysUser> findAll();

}