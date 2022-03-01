package com.bluetron.nb.common.JeeOnline.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bluetron.nb.common.base.result.Result;
import com.bluetron.nb.common.JeeOnline.dto.OnlCgformDto;
import com.bluetron.nb.common.JeeOnline.entity.OnlCgformButton;
import com.bluetron.nb.common.JeeOnline.entity.OnlCgformEnhanceJs;
import com.bluetron.nb.common.JeeOnline.entity.OnlCgformHead;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * @interfaceName: IOnlCgformHeadService
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/1/11 14:08
 */
public interface IOnlCgformHeadService extends IService<OnlCgformHead> {
    void initCopyState(List<OnlCgformHead> var1);
    Result<?> addAll(OnlCgformDto onlCgformDto);
    void doDbSynch(String var1, String var2) throws /*HibernateException,*/ IOException, TemplateException, SQLException;
    List<OnlCgformButton> queryButtonList(String code, boolean isListButton);
    OnlCgformEnhanceJs queryEnhanceJs(String formId, String cgJsType);
}
