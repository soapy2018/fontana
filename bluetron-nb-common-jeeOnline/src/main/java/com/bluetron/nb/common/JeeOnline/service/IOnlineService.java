package com.bluetron.nb.common.JeeOnline.service;

import com.bluetron.nb.common.JeeOnline.dto.OnlComplexModel;
import com.bluetron.nb.common.JeeOnline.entity.OnlCgformHead;

/**
 * @interfaceName: IOnlineService
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/1/14 14:22
 */
public interface IOnlineService {
    OnlComplexModel queryOnlineConfig(OnlCgformHead onlCgformHead);

}
