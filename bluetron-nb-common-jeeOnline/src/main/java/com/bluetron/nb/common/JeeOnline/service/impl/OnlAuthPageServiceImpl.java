package com.bluetron.nb.common.JeeOnline.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bluetron.nb.common.JeeOnline.entity.OnlAuthPage;
import com.bluetron.nb.common.JeeOnline.mapper.OnlAuthPageMapper;
import com.bluetron.nb.common.JeeOnline.service.IOnlAuthPageService;
import com.bluetron.nb.common.util.request.WebContextUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @className: OnlAuthPageServiceImpl
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/1/14 14:45
 */
@Service("onlAuthPageServiceImpl")
public class OnlAuthPageServiceImpl extends ServiceImpl<OnlAuthPageMapper, OnlAuthPage> implements IOnlAuthPageService {
    @Override
    public List<String> queryHideCode(String cgformId, boolean isList) {
        Long userId = WebContextUtil.takeTokenFromRequest().getUserId();
        //TODO 此处用户和用户权限表需要调整，包括userId的类型是Long还是String
        return this.baseMapper.queryRoleNoAuthCode(userId, cgformId, 5, isList ? 3 : 5, null);
    }
}


