package com.fontana.multitenancy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fontana.db.entity.SuperEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("tenant_info")
public class TenantInfo extends SuperEntity {
    private static final long serialVersionUID = -5948083712220365534L;
    private String tenantId;
    private String tenantName;
    private String datasourceUrl;
    private String datasourceUsername;
    private String datasourcePassword;
    private String datasourceDriver;
    /**
     * 是否启用 0-禁用 1-启用
     */
    private Boolean isEnable;
}
