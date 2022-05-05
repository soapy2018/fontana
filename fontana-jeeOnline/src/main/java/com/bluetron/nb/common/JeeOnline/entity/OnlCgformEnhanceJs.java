package com.fontana.JeeOnline.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @className: OnlCgformEnhanceJs
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/1/16 15:25
 */
@Data
@TableName("onl_cgform_enhance_js")
public class OnlCgformEnhanceJs implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(
            type = IdType.ASSIGN_UUID
    )
    private String id;
    private String cgformHeadId;
    private String cgJsType;
    private String cgJs;
    private String content;
}


