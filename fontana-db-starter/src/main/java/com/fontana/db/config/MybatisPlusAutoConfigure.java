package com.fontana.db.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.fontana.base.constant.CommonConstants;
import com.fontana.db.property.MybatisPlusAutoFillProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis-plus自动配置
 *
 * @author cqf
 * @date 2021/10/5
 * <p>
 */
@Configuration
@Slf4j
@EnableConfigurationProperties(MybatisPlusAutoFillProperties.class)
public class MybatisPlusAutoConfigure {

    @Autowired
    private MybatisPlusAutoFillProperties autoFillProperties;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = CommonConstants.MYBATISPLUS_PREFIX + ".auto-fill", name = "enabled", havingValue = "true", matchIfMissing = true)
    public MetaObjectHandler metaObjectHandler() {
        return new DateMetaObjectHandler(autoFillProperties);
    }

}
