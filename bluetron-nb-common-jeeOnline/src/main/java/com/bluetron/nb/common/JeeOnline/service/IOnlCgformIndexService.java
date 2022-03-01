package com.bluetron.nb.common.JeeOnline.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bluetron.nb.common.JeeOnline.entity.OnlCgformIndex;

/**
 * @interfaceName: IOnlCgformIndexService
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/1/12 15:49
 */
public interface IOnlCgformIndexService extends IService<OnlCgformIndex> {
    boolean isExistIndex(String countIndexSql);
    void createIndex(String code, String databaseType, String tableName);
}
