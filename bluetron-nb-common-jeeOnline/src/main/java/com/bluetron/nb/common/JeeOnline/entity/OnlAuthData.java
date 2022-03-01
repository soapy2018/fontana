package com.bluetron.nb.common.JeeOnline.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @className: OnlAuthData
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/1/16 16:35
 */
@Data
@TableName("onl_auth_data")
public class OnlAuthData implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(
            type = IdType.ASSIGN_ID
    )

    private String id;
    @Excel(
            name = "online表ID",
            width = 15.0D
    )
    private String cgformId;
    @Excel(
            name = "规则名",
            width = 15.0D
    )
    private String ruleName;
    @Excel(
            name = "规则列",
            width = 15.0D
    )
    private String ruleColumn;

    @Excel(
            name = "规则条件 大于小于like",
            width = 15.0D
    )
    private String ruleOperator;

    @Excel(
            name = "规则值",
            width = 15.0D
    )

    private String ruleValue;
    @Excel(
            name = "1有效 0无效",
            width = 15.0D
    )

    private Integer status;
    @JsonFormat(
            timezone = "GMT+8",
            pattern = "yyyy-MM-dd"
    )
    @DateTimeFormat(
            pattern = "yyyy-MM-dd"
    )
    private Date createTime;

    private String createBy;

    private String updateBy;

    @JsonFormat(
            timezone = "GMT+8",
            pattern = "yyyy-MM-dd"
    )
    @DateTimeFormat(
            pattern = "yyyy-MM-dd"
    )
    private Date updateTime;
}


