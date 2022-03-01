package com.bluetron.nb.common.JeeOnline.model;

import com.bluetron.nb.common.cloud.vo.SysPermissionDataRuleVo;
import com.bluetron.nb.common.JeeOnline.entity.OnlCgformField;
import lombok.Data;

import java.util.List;

/**
 * @className: OnlTableData
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/1/16 16:24
 */
@Data
public class OnlTableData {
    private String tableName;
    private String tableId;
    private List<OnlCgformField> allFieldList;
    private List<OnlCgformField> selectFieldList;
    private List<SysPermissionDataRuleVo> authList;
    private String mainField;
    private String joinField;
    private String alias;
    private boolean isMain;
}


