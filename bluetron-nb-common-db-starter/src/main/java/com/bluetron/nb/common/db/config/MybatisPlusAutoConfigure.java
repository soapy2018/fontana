package com.bluetron.nb.common.db.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.core.parser.ISqlParserFilter;
import com.baomidou.mybatisplus.extension.parsers.BlockAttackSqlParser;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantHandler;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantSqlParser;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.bluetron.nb.common.base.constant.CommonConstants;
import com.bluetron.nb.common.db.property.MybatisPlusAutoFillProperties;
import com.bluetron.nb.common.db.property.TenantProperties;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * mybatis-plus自动配置
 *
 * @author cqf
 * @date 2021/10/5
 * <p>
 * 
 
 */
@Slf4j
@EnableConfigurationProperties(MybatisPlusAutoFillProperties.class)
public class MybatisPlusAutoConfigure {
    @Autowired
    private TenantHandler tenantHandler;

    @Autowired
    private ISqlParserFilter sqlParserFilter;

    @Autowired
    private TenantProperties tenantProperties;

    @Autowired
    private MybatisPlusAutoFillProperties autoFillProperties;


    /**
     * 分页插件，自动识别数据库类型
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        boolean enableTenant = tenantProperties.getEnable();
        //是否开启多租户隔离
        if (enableTenant) {
            TenantSqlParser tenantSqlParser = new TenantSqlParser()
                    .setTenantHandler(tenantHandler);
            paginationInterceptor.setSqlParserList(CollectionUtils.arrayToList(tenantSqlParser));
            paginationInterceptor.setSqlParserFilter(sqlParserFilter);
        }else{
            List<ISqlParser> sqlParserList = new ArrayList<>();
            // 攻击 SQL 阻断解析器、加入解析链
            sqlParserList.add(new BlockAttackSqlParser());
            paginationInterceptor.setSqlParserList(sqlParserList);
        }
        return paginationInterceptor;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = CommonConstants.MYBATISPLUS_PREFIX + ".auto-fill", name = "enabled", havingValue = "true", matchIfMissing = true)
    public MetaObjectHandler metaObjectHandler() {
        return new DateMetaObjectHandler(autoFillProperties);
    }

    ///**********************动态数据源相关*******************/////////////
    /**
     * 配置文件yml中的默认数据源
     * @return
     */
    @Bean(name = "defaultDataSource")
    @ConditionalOnProperty(prefix = CommonConstants.DYNAMIC_DATASOURCE_PREFIX, name = "enabled", havingValue = "true")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource getDefaultDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * 初始化数据源，并用配置文件yml中的配置作为默认数据源
     * 只初始化一次
     * @return
     */
    @Bean("dynamicDataSource")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = CommonConstants.DYNAMIC_DATASOURCE_PREFIX, name = "enabled", havingValue = "true")
    public DataSource dynamicDataSource() {
        Map<Object, Object> dataSourceMap = new HashMap<>();
        HikariDataSource defaultDataSource = (HikariDataSource)getDefaultDataSource();
        dataSourceMap.put(CommonConstants.DEFAULT_TENANT, defaultDataSource);
// 不全部初始化了吧
//        List<TenantInfo> tenantList = tenantInfoService.getActiveTenantsList();
//        for (TenantInfo tenantInfo : tenantList) {
//            log.info(tenantInfo.toString());
//            HikariDataSource dataSource = new HikariDataSource();
//            dataSource.setDriverClassName(tenantInfo.getDatasourceDriver());
//            dataSource.setJdbcUrl(tenantInfo.getDatasourceUrl());
//            dataSource.setUsername(tenantInfo.getDatasourceUsername());
//            dataSource.setPassword(tenantInfo.getDatasourcePassword());
//            dataSource.setDataSourceProperties(defaultDataSource.getDataSourceProperties());
//            dataSourceMap.put(tenantInfo.getTenantId(), dataSource);
//        }
        return new DynamicDataSource(defaultDataSource, dataSourceMap);
    }

    @Bean
    @ConditionalOnProperty(prefix = CommonConstants.DYNAMIC_DATASOURCE_PREFIX, name = "enabled", havingValue = "true")
    public MybatisSqlSessionFactoryBean sqlSessionFactoryBean() throws Exception {
        MybatisSqlSessionFactoryBean sessionFactory = new MybatisSqlSessionFactoryBean();
        /**
         * 重点，使分页插件生效
         */
        Interceptor[] plugins = new Interceptor[1];
        plugins[0] = paginationInterceptor();
        sessionFactory.setPlugins(plugins);
        //配置数据源，此处配置为关键配置，如果没有将 dynamicDataSource作为数据源则不能实现切换
        sessionFactory.setDataSource(dynamicDataSource());
        //sessionFactory.setTypeAliasesPackage("com.sino.teamwork.*.*.entity,com.sino.teamwork.base.model");    // 扫描Model
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sessionFactory.setMapperLocations(resolver.getResources("classpath*:mapper/*/*Mapper.xml"));    // 扫描映射文件
        return sessionFactory;
    }
    @Bean
    @ConditionalOnProperty(prefix = CommonConstants.DYNAMIC_DATASOURCE_PREFIX, name = "enabled", havingValue = "true")
    public PlatformTransactionManager transactionManager() {
        // 配置事务管理, 使用事务时在方法头部添加@Transactional注解即可
        return new DataSourceTransactionManager(dynamicDataSource());
    }
    ///**********************动态数据源相关*******************/////////////


}
