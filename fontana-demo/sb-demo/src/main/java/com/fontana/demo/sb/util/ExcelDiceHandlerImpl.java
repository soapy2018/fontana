/**
 * Copyright (c) 2020-2022 宁波哥爱帮科技有限公司
 */
package com.fontana.demo.sb.util;

import cn.afterturn.easypoi.handler.inter.IExcelDictHandler;
import com.fontana.base.object.DictModel;
import com.fontana.redis.util.DictCacheHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author chenqingfeng
 * @description 字典处理handler
 * @date 2022/11/16 13:45
 */
@Component
public class ExcelDiceHandlerImpl implements IExcelDictHandler {

    @Autowired
    private DictCacheHelper dictCacheHelper;

    @Override
    @SuppressWarnings("unchecked")
    public List<Map> getList(String dict) {
        return dictCacheHelper.getDictItems(dict).stream().map(t -> {
            HashMap<String, String> dictItem = new HashMap<>(10);
            dictItem.put("dictKey", t.getValue());
            dictItem.put("dictValue", t.getText());
            return dictItem;
        }).collect(Collectors.toList());
        //return (List<Map>) dictCacheHelper.getDictItems(dict).stream().collect(Collectors.toMap(DictModel::getValue, DictModel::getText));
    }

    @Override
    public String toName(String dict, Object o, String name, Object value) {
        return dictCacheHelper.getDictItemByValue(dict, value.toString()).getText();
    }

    @Override
    public String toValue(String dict, Object o, String name, Object value) {
        return dictCacheHelper.getDictItemByName(dict, name).getValue();
    }
}