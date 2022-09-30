package com.fontana.db.property;

import com.fontana.base.constant.CommonConstants;
import com.fontana.base.constant.DataBaseConstant;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * mybatis-plus 配置
 *
 * @author cqf
 * @date 2020/10/5
 * <p>
 */
@Setter
@Getter
@ConfigurationProperties(prefix = CommonConstants.MYBATISPLUS_AUTOFILL_PREFIX)
@RefreshScope
public class MybatisPlusAutoFillProperties {
    /**
     * 是否开启自动填充字段
     */
    private Boolean enabled = true;
    /**
     * 是否开启了插入填充
     */
    private Boolean enableInsertFill = true;
    /**
     * 是否开启了更新填充
     */
    private Boolean enableUpdateFill = true;
    /**
     * 创建时间字段名
     */
    private String createTimeField = DataBaseConstant.CREATE_TIME;
    /**
     * 更新时间字段名
     */
    private String updateTimeField = DataBaseConstant.UPDATE_TIME;
    /**
     * 创建时间字段名
     */
    private String createUserIdField = DataBaseConstant.CREATE_USER;
    /**
     * 更新时间字段名
     */
    private String updateUserIdField = DataBaseConstant.UPDATE_USER;
}
