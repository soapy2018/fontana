package com.fontana.util.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import com.fontana.base.constant.CommonConstants;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户导出测试实例
 *
 * @Author: blcoud
 */
@Data
public class SysUserExcel implements Serializable {
    private static final long serialVersionUID = -5886012896705137070L;

    @Excel(name = "用户账号", width = 30, isImportField = "true_st")
    private String username;

    @Excel(name = "用户名", width = 30, isImportField = "true_st")
    private String nickname;

    @Excel(name = "手机号码", width = 30, isImportField = "true_st")
    private String mobile;

    @Excel(name = "性别", replace = {"男_0", "女_1"}, isImportField = "true_st")
    private Integer sex;

    @Excel(name = "所属租户", width = 32, isImportField = "true_st")
    private String tenantId;

    @ExcelCollection(name = "地址", orderNum = "4")
    private List<SysUserAddress> addresses;

    @Excel(name = "创建时间", format = CommonConstants.DATETIME_FORMAT,  width = 20)
    private Date createTime;

    @Excel(name = "修改时间", format = CommonConstants.DATETIME_FORMAT,  width = 20)
    private Date updateTime;
}
