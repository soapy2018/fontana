package com.fontana.util.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.fontana.base.constant.CommonConstants;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户导出测试实例
 *
 * @Author: blcoud
 */
@Data
public class SysUserAddress implements Serializable {
    private static final long serialVersionUID = -5886012896705137070L;

    @Excel(name = "用户账号", width = 30, isImportField = "true_st")
    private String username;

    @Excel(name = "地址", width = 30, isImportField = "true_st")
    private String address;

}
