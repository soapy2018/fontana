package com.fontana.demo.sb.model;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
import com.fontana.base.constant.CommonConstants;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户导出测试实例
 *
 * @Author: cqf
 */
@Data
public class SysUserExcel implements Serializable {
    private static final long serialVersionUID = -5886012896705137070L;

    @Excel(name = "用户账号", width = 30, needMerge = true, isImportField = "true_st")
    private String username;

    @Excel(name = "用户名", width = 30, needMerge = true, isImportField = "true_st")
    private String nickname;

    @Excel(name = "手机号码", width = 30, needMerge = true, isImportField = "true_st")
    private String mobile;

    @Excel(name = "性别", replace = {"男_0", "女_1"}, needMerge = true, addressList = true, isImportField = "true_st")
    private Integer sex;

    @Excel(name = "所属租户", width = 32, needMerge = true, isImportField = "true_st")
    private String tenantId;

    @Excel(name = "用户状态", dict = "sys_user_status", needMerge = true, addressList = true, isImportField = "true_st")
    private Integer userStatus;

    @ExcelCollection(name = "地址", orderNum = "4")
    private List<SysUserAddress> addresses;

    @Excel(name = "创建时间", needMerge = true, isColumnHidden = true, format = CommonConstants.DATETIME_FORMAT,  width = 20)
    private Date createTime;

//    @ExcelIgnore
//    @Excel(name = "修改时间", format = CommonConstants.DATETIME_FORMAT,  width = 20)
    private Date updateTime;
}
