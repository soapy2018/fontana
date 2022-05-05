package com.fontana.JeeOnline.service;

import com.fontana.JeeOnline.dto.OnlComplexModel;
import com.fontana.JeeOnline.entity.OnlCgformHead;

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
