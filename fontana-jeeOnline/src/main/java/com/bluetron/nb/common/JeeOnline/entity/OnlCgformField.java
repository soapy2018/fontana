package com.fontana.JeeOnline.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @className: OnlCgformField
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/1/12 14:39
 */
@Data
@TableName("onl_cgform_field")
public class OnlCgformField implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(
            type = IdType.ASSIGN_UUID
    )
    private String id;
    private String cgformHeadId;
    private String dbFieldName;
    private String dbFieldTxt;
    private String dbFieldNameOld;
    private Integer dbIsKey;
    private Integer dbIsNull;
    private String dbType;
    private Integer dbLength;
    private Integer dbPointLength;
    private String dbDefaultVal;
    private String dictField;
    private String dictTable;
    private String dictText;
    private String fieldShowType;
    private String fieldHref;
    private Integer fieldLength;
    private String fieldValidType;
    private String fieldMustInput;
    private String fieldExtendJson;
    private String fieldDefaultValue;
    private Integer isQuery;
    private Integer isShowForm;
    private Integer isShowList;
    private Integer isReadOnly;
    private String queryMode;
    private String mainTable;
    private String mainField;
    private Integer orderNum;
    private String updateBy;
    @JsonFormat(
            timezone = "GMT+8",
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date updateTime;
    @JsonFormat(
            timezone = "GMT+8",
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date createTime;
    private String createBy;
    private String converter;
    private String queryConfigFlag;
    private String queryDefVal;
    private String queryDictText;
    private String queryDictField;
    private String queryDictTable;
    private String queryShowType;
    private String queryValidType;
    private String queryMustInput;
    private String sortFlag;
    private transient String alias;
}


