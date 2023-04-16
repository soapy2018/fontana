package com.fontana.JeeOnline.dto;

import com.fontana.cloud.vo.DictVo;
import com.fontana.JeeOnline.entity.OnlCgformButton;
import com.fontana.JeeOnline.model.ForeignKey;
import com.fontana.JeeOnline.model.HrefSlots;
import com.fontana.JeeOnline.model.OnlColumn;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @className: OnlComplexModel
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/1/14 13:54
 */
@Data
public class OnlComplexModel implements Serializable {
    private static final long se = 1L;
    List<HrefSlots> fieldHrefSlots;
    private String code;
    private String formTemplate;
    private String description;
    private String currentTableName;
    private Integer tableType;
    private String paginationFlag;
    private String checkboxFlag;
    private Integer scrollFlag;
    private List<OnlColumn> columns;
    private List<String> hideColumns;
    private Map<String, List<DictVo>> dictOptions = new HashMap();
    private List<OnlCgformButton> cgButtonList;
    private String enhanceJs;
    private List<ForeignKey> foreignKeys;
    private String pidField;
    private String hasChildrenField;
    private String textField;
    private String isDesForm;
    private String desFormCode;
    private Integer relationType;
}


