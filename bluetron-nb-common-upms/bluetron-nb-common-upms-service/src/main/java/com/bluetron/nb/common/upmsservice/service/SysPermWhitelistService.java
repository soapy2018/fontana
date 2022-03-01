package com.bluetron.nb.common.upmsservice.service;

import com.bluetron.nb.common.db.service.IBaseService;
import com.bluetron.nb.common.upmsservice.entity.SysPermWhitelist;

import java.util.List;

/**
 * 白名单数据服务接口。
 *
 * @author cqf
 * @date 2020-08-08
 */
public interface SysPermWhitelistService extends IBaseService<SysPermWhitelist, String> {

    /**
     * 获取白名单权限资源的列表。
     *
     * @return 白名单权限资源地址列表。
     */
    List<String> getWhitelistPermList();
}
