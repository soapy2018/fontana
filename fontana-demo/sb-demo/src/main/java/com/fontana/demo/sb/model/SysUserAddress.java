package com.fontana.demo.sb.model;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户导出测试实例
 *
 * @Author: cqf
 */
@Data
public class SysUserAddress implements Serializable {
    private static final long serialVersionUID = -5886012896705137070L;

    @Excel(name = "用户账号", width = 30, isImportField = "true_st")
    private String username;

    @Excel(name = "地址", width = 30, isImportField = "true_st")
    private String address;

}
