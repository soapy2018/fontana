package com.bluetron.nb.common.JeeOnline.service;

import java.util.List;

/**
 * @interfaceName: IOnlAuthPageService
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/1/14 14:45
 */
public interface IOnlAuthPageService {
    List<String> queryHideCode(String cgformId, boolean isList);

}
