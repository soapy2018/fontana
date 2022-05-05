package com.fontana.JeeOnline.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fontana.cloud.vo.SysPermissionDataRuleVo;
import com.fontana.JeeOnline.entity.OnlAuthData;
import com.fontana.JeeOnline.mapper.OnlAuthDataMapper;
import com.fontana.JeeOnline.service.IOnlAuthDataService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @className: OnlAuthDataServiceImpl
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/1/16 16:40
 */
@Service("onlAuthDataServiceImpl")
public class OnlAuthDataServiceImpl extends ServiceImpl<OnlAuthDataMapper, OnlAuthData> implements IOnlAuthDataService {
    @Override
    public void deleteOne(String var1) {

    }

    @Override
    public List<SysPermissionDataRuleVo> queryUserOnlineAuthData(String var1, String var2) {
        return null;
    }
}


