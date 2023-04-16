package com.fontana.JeeOnline.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fontana.cloud.vo.SysPermissionDataRuleVo;
import com.fontana.JeeOnline.entity.OnlAuthData;

import java.util.List;

/**
 * @interfaceName: IOnlAuthDataService
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/1/16 16:39
 */
public interface IOnlAuthDataService extends IService<OnlAuthData> {
    void deleteOne(String var1);

    List<SysPermissionDataRuleVo> queryUserOnlineAuthData(String var1, String var2);
}
