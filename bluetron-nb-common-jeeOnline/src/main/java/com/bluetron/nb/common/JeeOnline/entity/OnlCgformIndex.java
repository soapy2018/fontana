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
 * @className: OnlCgformIndex
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/1/12 14:40
 */
@Data
@TableName("onl_cgform_index")
public class OnlCgformIndex implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(
            type = IdType.ASSIGN_UUID
    )
    private String id;
    private String cgformHeadId;
    private String indexName;
    private String indexField;
    private String isDbSynch;
    private Integer delFlag;
    private String indexType;
    private String createBy;
    @JsonFormat(
            timezone = "GMT+8",
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date createTime;
    private String updateBy;
    @JsonFormat(
            timezone = "GMT+8",
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private Date updateTime;
}


