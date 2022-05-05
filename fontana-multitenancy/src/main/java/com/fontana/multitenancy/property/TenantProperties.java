package com.fontana.multitenancy.property;

import com.fontana.base.constant.CommonConstants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.ArrayList;
import java.util.List;

/**
 * 多租户配置
 *
 * @author cqf
 * @date 2019/8/5
 */
@Setter
@Getter
@ConfigurationProperties(prefix = CommonConstants.TENANT_PREFIX)
@RefreshScope
public class TenantProperties {
    /**
     * 是否开启多租户
     */
    private Boolean enable = false;

    /**
     * 多租户隔离类型,db-schema隔离；tb-字段隔离
     */
    private String type;

    /**
     * 配置数据表中的tenant_id字段作为多租户隔离字段，只在table隔离下有用
     */
    private String tenantIdColumn = "tenant_id";

    /**
     * 配置不进行多租户隔离的表名，只在table隔离下有用
     */
    private List<String> ignoreTables = new ArrayList<>();

    /**
     * 配置不进行多租户隔离的sql，只在table隔离下有用
     * 需要配置mapper的全路径如：com.bluetron.app.bcloud.user.mapper.SysUserMapper.findList
     */
    private List<String> ignoreSqls = new ArrayList<>();
}
