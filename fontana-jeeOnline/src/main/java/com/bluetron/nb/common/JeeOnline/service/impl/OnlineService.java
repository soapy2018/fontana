package com.fontana.JeeOnline.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fontana.cloud.feign.system.api.ISysBaseAPI;
import com.fontana.cloud.vo.DictVo;
import com.fontana.JeeOnline.dto.OnlComplexModel;
import com.fontana.JeeOnline.entity.OnlCgformButton;
import com.fontana.JeeOnline.entity.OnlCgformField;
import com.fontana.JeeOnline.entity.OnlCgformHead;
import com.fontana.JeeOnline.form.LinkDown;
import com.fontana.JeeOnline.model.ForeignKey;
import com.fontana.JeeOnline.model.HrefSlots;
import com.fontana.JeeOnline.model.OnlColumn;
import com.fontana.JeeOnline.service.IOnlAuthPageService;
import com.fontana.JeeOnline.service.IOnlCgformFieldService;
import com.fontana.JeeOnline.service.IOnlCgformHeadService;
import com.fontana.JeeOnline.service.IOnlineService;
import com.fontana.JeeOnline.util.OnlineServiceUtil;
import com.fontana.util.lang.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @className: OnlineService
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/1/14 14:22
 */
public class OnlineService implements IOnlineService {

    @Autowired
    private IOnlCgformHeadService onlCgformHeadService;

    @Autowired
    private IOnlCgformFieldService onlCgformFieldService;

    @Autowired
    private IOnlAuthPageService onlAuthPageService;

    @Autowired
    private ISysBaseAPI sysBaseAPI;

    @Override
    public OnlComplexModel queryOnlineConfig(OnlCgformHead onlCgformHead) {
        String headId = onlCgformHead.getId();
        boolean isMultiPage = OnlineServiceUtil.isMultiPage(onlCgformHead);
        List fieldList = this.listFields(headId);
        List hideCodeList = this.onlAuthPageService.queryHideCode(headId, true);
        HashMap dictModelMap = new HashMap();
        ArrayList hrefSlotsList = new ArrayList();
        ArrayList foreignList = new ArrayList();
        ArrayList linkFieldList = new ArrayList();
        ArrayList onlColumnList = new ArrayList();

        HashMap var12 = new HashMap();

        Iterator iterator = fieldList.iterator();
        while (iterator.hasNext()) {
            OnlCgformField onlCgformField = (OnlCgformField) iterator.next();
            String dbFieldName = onlCgformField.getDbFieldName();
            String mainTable = onlCgformField.getMainTable();
            String mainField = onlCgformField.getMainField();
            if (StringUtil.isNotEmpty(mainField) && StringUtil.isNotEmpty(mainTable)) {
                ForeignKey foreignKey = new ForeignKey(dbFieldName, mainField);
                foreignList.add(foreignKey);
            }

            if (1 == onlCgformField.getIsShowList() && !"id".equals(dbFieldName) && !hideCodeList.contains(dbFieldName) && !linkFieldList.contains(dbFieldName)) {
                OnlColumn onlColumn = this.getOnlColumn(onlCgformField, dictModelMap, hrefSlotsList);
                String linkField = onlColumn.getLinkField();
                if (linkField != null && !"".equals(linkField)) {
                    this.getLinkFields(fieldList, linkFieldList, onlColumnList, dbFieldName, linkField);
                }

                var12.put(onlCgformField.getDbFieldName(), 1);
                onlColumnList.add(onlColumn);
            }
        }

        if (isMultiPage) {
            List list = this.getOnlColumnList(onlCgformHead, dictModelMap, hrefSlotsList, var12);
            if (list.size() > 0) {
                ArrayList mainTableList = new ArrayList();
                iterator = var12.keySet().iterator();

                while (iterator.hasNext()) {
                    String mainTable = (String) iterator.next();
                    if ((Integer) var12.get(mainTable) > 1) {
                        mainTableList.add(mainTable);
                    }
                }

                OnlColumn onlColumn;
                for (iterator = list.iterator(); iterator.hasNext(); onlColumnList.add(onlColumn)) {
                    onlColumn = (OnlColumn) iterator.next();
                    String dataIndex = onlColumn.getDataIndex();
                    if (mainTableList.contains(dataIndex)) {
                        onlColumn.setDataIndex(onlColumn.getTableName() + "_" + dataIndex);
                    }
                }
            }
        }

        OnlComplexModel onlComplexModel = new OnlComplexModel();
        onlComplexModel.setCode(headId);
        onlComplexModel.setTableType(onlCgformHead.getTableType());
        onlComplexModel.setFormTemplate(onlCgformHead.getFormTemplate());
        onlComplexModel.setDescription(onlCgformHead.getTableTxt());
        onlComplexModel.setCurrentTableName(onlCgformHead.getTableName());
        onlComplexModel.setPaginationFlag(onlCgformHead.getIsPage());
        onlComplexModel.setCheckboxFlag(onlCgformHead.getIsCheckbox());
        onlComplexModel.setScrollFlag(onlCgformHead.getScroll());
        onlComplexModel.setRelationType(onlCgformHead.getRelationType());
        onlComplexModel.setColumns(onlColumnList);
        onlComplexModel.setDictOptions(dictModelMap);
        onlComplexModel.setFieldHrefSlots(hrefSlotsList);
        onlComplexModel.setForeignKeys(foreignList);
        onlComplexModel.setHideColumns(hideCodeList);

        List buttonList = this.onlCgformHeadService.queryButtonList(headId, true);
        ArrayList onlCgformButtonList = new ArrayList();
        iterator = buttonList.iterator();
        while (iterator.hasNext()) {
            OnlCgformButton onlCgformButton = (OnlCgformButton) iterator.next();
            if (!hideCodeList.contains(onlCgformButton.getButtonCode())) {
                onlCgformButtonList.add(onlCgformButton);
            }
        }
        onlComplexModel.setCgButtonList(onlCgformButtonList);

//  OnlCgformEnhanceJs onlCgformEnhanceJs = this.onlCgformHeadService.queryEnhanceJs(headId, "list");
//  if (onlCgformEnhanceJs != null && StringUtil.isNotEmpty(onlCgformEnhanceJs.getCgJs())) {
//   var17 = EnhanceJsUtil.b(onlCgformEnhanceJs.getCgJs(), buttonList);
//   onlComplexModel.setEnhanceJs(var17);
//  }

        if ("Y".equals(onlCgformHead.getIsTree())) {
            onlComplexModel.setPidField(onlCgformHead.getTreeParentIdField());
            onlComplexModel.setHasChildrenField(onlCgformHead.getTreeIdField());
            onlComplexModel.setTextField(onlCgformHead.getTreeFieldname());
        }

        return onlComplexModel;
    }

    private List<OnlCgformField> listFields(String headId) {
        LambdaQueryWrapper<OnlCgformField> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OnlCgformField::getCgformHeadId, headId);
        lambdaQueryWrapper.orderByAsc(OnlCgformField::getOrderNum);
        return this.onlCgformFieldService.list(lambdaQueryWrapper);
    }

    private OnlColumn getOnlColumn(OnlCgformField onlCgformField, Map<String, List<DictVo>> dictModelMap, List<HrefSlots> hrefSlotsList) {
        String dbFieldName = onlCgformField.getDbFieldName();
        OnlColumn onlColumn = new OnlColumn(onlCgformField.getDbFieldTxt(), dbFieldName);
        String dictField = onlCgformField.getDictField();
        String fieldShowType = onlCgformField.getFieldShowType();
        if (StringUtil.isNotEmpty(dictField) && !"popup".equals(fieldShowType)) {
            List DitList = new ArrayList();
            if (StringUtil.isNotEmpty(onlCgformField.getDictTable())) {
                DitList = this.sysBaseAPI.queryTableDictItemsByCode(onlCgformField.getDictTable(), onlCgformField.getDictText(), dictField);
            } else if (StringUtil.isNotEmpty(onlCgformField.getDictField())) {
                DitList = this.sysBaseAPI.queryDictItemsByCode(dictField);
            }

            dictModelMap.put(dbFieldName, DitList);
            onlColumn.setCustomRender(dbFieldName);
        }

        if ("switch".equals(fieldShowType)) {
            List switchDictList = OnlineServiceUtil.getSwitchDict(onlCgformField);
            dictModelMap.put(dbFieldName, switchDictList);
            onlColumn.setCustomRender(dbFieldName);
        }

        if ("link_down".equals(fieldShowType)) {
            String dictTable = onlCgformField.getDictTable();
            LinkDown linkDown = JSONObject.parseObject(dictTable, LinkDown.class);
            List dictItemList = this.sysBaseAPI.queryTableDictItemsByCode(linkDown.getTable(), linkDown.getTxt(), linkDown.getKey());
            dictModelMap.put(dbFieldName, dictItemList);
            onlColumn.setCustomRender(dbFieldName);
            onlColumn.setLinkField(linkDown.getLinkField());
        }


        if ("sel_tree".equals(fieldShowType)) {
            String[] dictText = onlCgformField.getDictText().split(",");
            List dictItemList = this.sysBaseAPI.queryTableDictItemsByCode(onlCgformField.getDictTable(), dictText[2], dictText[0]);
            dictModelMap.put(dbFieldName, dictItemList);
            onlColumn.setCustomRender(dbFieldName);
        }

        if ("cat_tree".equals(fieldShowType)) {
            String dictText = onlCgformField.getDictText();
            if (StringUtil.isEmpty(dictText)) {
                String codeLikeSQL = OnlineServiceUtil.getCodeLikeSQL(onlCgformField.getDictField());
                List dictItemList = this.sysBaseAPI.queryFilterTableDictInfo("SYS_CATEGORY", "NAME", "ID", codeLikeSQL);
                dictModelMap.put(dbFieldName, dictItemList);
                onlColumn.setCustomRender(dbFieldName);
            } else {
                onlColumn.setCustomRender("_replace_text_" + dictText);
            }
        }

        if ("sel_depart".equals(fieldShowType)) {
            String[] fieldExtendJsonContext = this.getFieldExtendJsonContext(onlCgformField.getFieldExtendJson());
            String id = fieldExtendJsonContext[0].length() > 0 ? fieldExtendJsonContext[0] : "ID";
            String departName = fieldExtendJsonContext[1].length() > 0 ? fieldExtendJsonContext[1] : "DEPART_NAME";
            List tableDictItemsByCode = this.sysBaseAPI.queryTableDictItemsByCode("SYS_DEPART", departName, id);
            dictModelMap.put(dbFieldName, tableDictItemsByCode);
            onlColumn.setCustomRender(dbFieldName);
        }

        if ("sel_user".equals(onlCgformField.getFieldShowType())) {
            String[] fieldExtendJsonContext = this.getFieldExtendJsonContext(onlCgformField.getFieldExtendJson());
            String userName = fieldExtendJsonContext[0].length() > 0 ? fieldExtendJsonContext[0] : "USERNAME";
            String realName = fieldExtendJsonContext[1].length() > 0 ? fieldExtendJsonContext[1] : "REALNAME";
            List tableDictItemsByCode = this.sysBaseAPI.queryTableDictItemsByCode("SYS_USER", realName, userName);
            dictModelMap.put(dbFieldName, tableDictItemsByCode);
            onlColumn.setCustomRender(dbFieldName);
        }

        if (fieldShowType.indexOf("file") >= 0) {
            onlColumn.setScopedSlots("fileSlot");
        } else if (fieldShowType.indexOf("image") >= 0) {
            onlColumn.setScopedSlots("imgSlot");
        } else if (fieldShowType.indexOf("editor") >= 0) {
            onlColumn.setScopedSlots("htmlSlot");
        } else if (fieldShowType.equals("date")) {
            onlColumn.setScopedSlots("dateSlot");
        } else if (fieldShowType.equals("pca")) {
            onlColumn.setScopedSlots("pcaSlot");
        }

        if (StringUtil.isNotBlank(onlCgformField.getFieldHref())) {
            String hrefSlotName = "fieldHref_" + dbFieldName;
            onlColumn.setHrefSlotName(hrefSlotName);
            hrefSlotsList.add(new HrefSlots(hrefSlotName, onlCgformField.getFieldHref()));
        }

        if ("1".equals(onlCgformField.getSortFlag())) {
            onlColumn.setSorter(true);
        }

        String fieldExtendJson = onlCgformField.getFieldExtendJson();
        if (StringUtil.isNotEmpty(fieldExtendJson) && fieldExtendJson.indexOf("showLength") > 0) {
            JSONObject jsonObject = JSON.parseObject(fieldExtendJson);
            if (jsonObject != null && jsonObject.get("showLength") != null) {
                onlColumn.setShowLength(Integer.parseInt(jsonObject.get("showLength").toString()));
            }
        }

        return onlColumn;
    }


    private String[] getFieldExtendJsonContext(String fieldExtendJson) {
        String[] fieldExtendJsonContext = new String[]{"", ""};
        if (fieldExtendJson != null && !"".equals(fieldExtendJson)) {
            JSONObject jsonObject = JSON.parseObject(fieldExtendJson);
            if (jsonObject.containsKey("store")) {
                fieldExtendJsonContext[0] = StringUtil.camelToUnderline(jsonObject.getString("store"));
            }

            if (jsonObject.containsKey("text")) {
                fieldExtendJsonContext[1] = StringUtil.camelToUnderline(jsonObject.getString("text"));
            }
        }
        return fieldExtendJsonContext;
    }

    private void getLinkFields(List<OnlCgformField> onlCgformFieldList, List<String> linkFieldList, List<OnlColumn> onlColumnList, String dbFieldName, String linkField) {
        if (StringUtil.isNotEmpty(linkField)) {
            String[] linkFields = linkField.split(",");
            for (int i = 0; i < linkFields.length; ++i) {
                String link = linkFields[i];
                Iterator iterator = onlCgformFieldList.iterator();

                while (iterator.hasNext()) {
                    OnlCgformField onlCgformField = (OnlCgformField) iterator.next();
                    String fieldName = onlCgformField.getDbFieldName();
                    if (1 == onlCgformField.getIsShowList() && link.equals(fieldName)) {
                        linkFieldList.add(link);
                        OnlColumn onlColumn = new OnlColumn(onlCgformField.getDbFieldTxt(), fieldName);
                        onlColumn.setCustomRender(dbFieldName);
                        onlColumnList.add(onlColumn);
                        break;
                    }
                }
            }
        }

    }

    private List<OnlColumn> getOnlColumnList(OnlCgformHead onlCgformHead, Map<String, List<DictVo>> dictMap, List<HrefSlots> hrefSlotsList, Map<String, Integer> var4) {
        int tableType = onlCgformHead.getTableType();
        ArrayList onlColumnList = new ArrayList();
        if (tableType == 2) {
            String subTableStr = onlCgformHead.getSubTableStr();
            if (subTableStr != null && !"".equals(subTableStr)) {
                String[] strings = subTableStr.split(",");
                label46:
                for (int i = 0; i < strings.length; ++i) {
                    String subTable = strings[i];
                    LambdaQueryWrapper lambdaQueryWrapper = new LambdaQueryWrapper<OnlCgformHead>().eq(OnlCgformHead::getTableName, subTable);
                    OnlCgformHead subOnlCgformHead = this.onlCgformHeadService.getOne(lambdaQueryWrapper);
                    if (subOnlCgformHead != null) {
                        List cgformFieldList = this.ListCgformField(subOnlCgformHead.getId());
                        Iterator iterator = cgformFieldList.iterator();

                        while (true) {
                            OnlCgformField onlCgformField;
                            do {
                                if (!iterator.hasNext()) {
                                    continue label46;
                                }

                                onlCgformField = (OnlCgformField) iterator.next();
                            } while (1 != onlCgformField.getIsShowList() && 1 != onlCgformField.getIsQuery());

                            String dbFieldName = onlCgformField.getDbFieldName();
                            if (!"id".equals(dbFieldName)) {
                                Integer var19 = var4.get(dbFieldName);
                                if (var19 == null) {
                                    var4.put(dbFieldName, 1);
                                } else {
                                    var4.put(dbFieldName, var19 + 1);
                                }

                                OnlColumn onlColumn = this.getOnlColumn(onlCgformField, dictMap, hrefSlotsList);
                                if (1 == onlCgformField.getIsShowList()) {
                                    onlColumn.setTableName(onlCgformHead.getTableName());
                                    onlColumnList.add(onlColumn);
                                }
                            }
                        }
                    }
                }
            }
        }

        return onlColumnList;
    }

    private List<OnlCgformField> ListCgformField(String headId) {
        LambdaQueryWrapper<OnlCgformField> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(OnlCgformField::getCgformHeadId, headId);
        lambdaQueryWrapper.orderByAsc(OnlCgformField::getOrderNum);
        return this.onlCgformFieldService.list(lambdaQueryWrapper);
    }

}


