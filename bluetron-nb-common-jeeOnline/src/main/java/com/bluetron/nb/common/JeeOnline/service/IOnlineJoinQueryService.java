package com.bluetron.nb.common.JeeOnline.service;

import com.bluetron.nb.common.JeeOnline.entity.OnlCgformHead;

import java.util.Map;

/**
 * @interfaceName: IOnlineJoinQueryService
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/1/16 16:03
 */
public interface IOnlineJoinQueryService {

    Map<String, Object> pageList(OnlCgformHead var1, Map<String, Object> var2, boolean var3);

    Map<String, Object> pageList(OnlCgformHead var1, Map<String, Object> var2);

}
