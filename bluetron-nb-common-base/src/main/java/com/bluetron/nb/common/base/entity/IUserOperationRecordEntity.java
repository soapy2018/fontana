package com.bluetron.nb.common.base.entity;

import java.util.Date;

/**
 * 所有带用户操作记录功能实体的汇总
 *
 * @author genx
 * @date 2021/4/8 10:42
 */
public interface IUserOperationRecordEntity {

    String getCreateUsername();

    void setCreateUsername(String createUsername);

    String getCreateRealname();

    void setCreateRealname(String createRealname);

    Date getCreateTime();

    void setCreateTime(Date createTime);

    String getUpdateUsername();

    void setUpdateUsername(String updateUsername);

    String getUpdateRealname();

    void setUpdateRealname(String updateRealname);

    Date getUpdateTime();

    void setUpdateTime(Date updateTime);

}
