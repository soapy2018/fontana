package com.bluetron.nb.common.JeeOnline.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bluetron.nb.common.cloud.vo.DictVo;
import com.bluetron.nb.common.JeeOnline.entity.OnlCgformField;
import com.bluetron.nb.common.JeeOnline.entity.OnlCgformHead;
import com.bluetron.nb.common.util.lang.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @className: b
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/1/14 14:30
 */
public class OnlineServiceUtil {
    public static boolean isMultiPage(OnlCgformHead onlCgformHead) {
        if (onlCgformHead.getTableType() == 2) {
            String themeTemplate = onlCgformHead.getThemeTemplate();
            if ("erp".equals(themeTemplate) || "innerTable".equals(themeTemplate) || "Y".equals(onlCgformHead.getIsTree())) {
                return false;
            }

            String extConfigJson = onlCgformHead.getExtConfigJson();
            if (extConfigJson != null && !"".equals(extConfigJson)) {
                JSONObject jsonObject = JSON.parseObject(extConfigJson);
                return jsonObject.containsKey("joinQuery") && 1 == jsonObject.getInteger("joinQuery");
            }
        }
        return false;
    }

    public static List<DictVo> getSwitchDict(OnlCgformField onlCgformField) {
        ArrayList dictList = new ArrayList();
        String fieldExtendJson = onlCgformField.getFieldExtendJson();
        String yes = "是";
        String no = "否";
        JSONArray jsonArray = JSONArray.parseArray("[\"Y\",\"N\"]");
        if (StringUtil.isNotEmpty(fieldExtendJson)) {
            jsonArray = JSONArray.parseArray(fieldExtendJson);
        }

        DictVo dictYes = new DictVo(jsonArray.getString(0), yes);
        DictVo dictNo = new DictVo(jsonArray.getString(1), no);
        dictList.add(dictYes);
        dictList.add(dictNo);
        return dictList;
    }

    public static String getCodeLikeSQL(String dictField) {
        return dictField != null && !"".equals(dictField) && !"0".equals(dictField) ? "CODE like '" + dictField + "%" + "'" : "";
    }

    public static Map<String, Object> getParameterMap(HttpServletRequest httpServletRequest) {
        Map requestParameterMap = httpServletRequest.getParameterMap();
        HashMap parameterMap = new HashMap();
        Iterator iterator = requestParameterMap.entrySet().iterator();
        String parameterName = "";
        String parameterValueStr = "";

        for(Object parameterValue; iterator.hasNext(); parameterMap.put(parameterName, parameterValueStr)) {
            Map.Entry entry = (Map.Entry)iterator.next();
            parameterName = (String)entry.getKey();
            parameterValue = entry.getValue();
            if (!"_t".equals(parameterName) && null != parameterValue) {
                if (!(parameterValue instanceof String[])) {
                    parameterValueStr = parameterValue.toString();
                } else {
                    String[] parameterValues = (String[]) parameterValue;
                    for(int i = 0; i < parameterValues.length; ++i) {
                        parameterValueStr = parameterValues[i] + ",";
                    }

                    parameterValueStr = parameterValueStr.substring(0, parameterValueStr.length() - 1);
                }
            } else {
                parameterValueStr = "";
            }
        }
        return parameterMap;
    }
}


