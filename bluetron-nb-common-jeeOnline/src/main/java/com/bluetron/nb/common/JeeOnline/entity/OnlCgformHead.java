package com.bluetron.nb.common.JeeOnline.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.util.Date;

/**
 * @className: OnlCgformHead
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/1/11 14:09
 */
@Data
@TableName("onl_cgform_head")
public class OnlCgformHead implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(
            type = IdType.ASSIGN_UUID
    )
    private String id;
    private String tableName;
    //'表类型: 1单表、2主表、3附表'
    private Integer tableType;
    private Integer tableVersion;
    private String tableTxt;
    private String isCheckbox;
    private String isDbSynch;
    private String isPage;
    private String isTree;
    private String idSequence;
    private String idType;
    private String queryMode;
    private Integer relationType;
    private String subTableStr;
    private Integer tabOrderNum;
    private String treeParentIdField;
    private String treeIdField;
    private String treeFieldname;
    private String formCategory;
    private String formTemplate;
    private String themeTemplate;
    private String formTemplateMobile;
    private String extConfigJson;
    private String updateBy;
    @JsonFormat(
            timezone = "GMT+8",
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date updateTime;
    private String createBy;
    @JsonFormat(
            timezone = "GMT+8",
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date createTime;
    private Integer copyType;
    private Integer copyVersion;
    private String physicId;
    private transient Integer hascopy;
    private Integer scroll;
    private transient String taskId;
    private String isDesForm;
    private String desFormCode;
    private String lowAppId;

}


