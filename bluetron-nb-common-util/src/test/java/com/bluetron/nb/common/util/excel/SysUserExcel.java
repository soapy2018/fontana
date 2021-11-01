package com.bluetron.nb.common.util.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.bluetron.nb.common.base.constant.CommonConstants;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户导出测试实例
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

    @Excel(name = "性别", replace = { "男_0", "女_1" }, isImportField = "true_st")
    private Integer sex;

    @Excel(name = "所属租户", width = 32, isImportField = "true_st")
    private String tenantId;

    @Excel(name = "创建时间", format = CommonConstants.DATETIME_FORMAT, isImportField = "true_st", width = 20)
    private Date createTime;

    @Excel(name = "修改时间", format = CommonConstants.DATETIME_FORMAT, isImportField = "true_st", width = 20)
    private Date updateTime;
}
