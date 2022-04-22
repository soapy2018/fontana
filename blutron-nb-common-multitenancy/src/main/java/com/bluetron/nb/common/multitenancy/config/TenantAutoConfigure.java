package com.bluetron.nb.common.multitenancy.config;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.core.parser.ISqlParserFilter;
import com.baomidou.mybatisplus.core.parser.SqlParserHelper;
import com.baomidou.mybatisplus.extension.parsers.BlockAttackSqlParser;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantHandler;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantSqlParser;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.bluetron.nb.common.base.constant.CommonConstants;
import com.bluetron.nb.common.base.context.TenantContextHolder;
import com.bluetron.nb.common.multitenancy.property.TenantProperties;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.StringValue;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 多租户自动配置
 *
 * @author cqf
 * @date 2021/8/5
 */
@Configuration
@EnableConfigurationProperties({TenantProperties.class, DataSourceProperties.class})
@Slf4j
public class TenantAutoConfigure {

    @Autowired
    private TenantProperties tenantProperties;

    @Autowired
    private DataSourceProperties dataSourceProperties;

    ///**********************字段级隔离配置*******************/////////////
    @Bean
    @ConditionalOnProperty(prefix = CommonConstants.TENANT_PREFIX, name = "type", havingValue = "tb")
    public TenantHandler tenantHandler() {
        return new TenantHandler() {
            /**
             * 获取租户id
             */
            @Override
            public Expression getTenantId(boolean where) {
                String tenant = TenantContextHolder.getTenant();
                if (tenant != null) {
                    return new StringValue(TenantContextHolder.getTenant());
                }
                return new NullValue();
            }

            /**
             * 获取租户列名
             */
            @Override
            public String getTenantIdColumn() {
                return tenantProperties.getTenantIdColumn();
            }

            /**
             * 过滤不需要根据租户隔离的表
             * @param tableName 表名
             */
            @Override
            public boolean doTableFilter(String tableName) {
                return tenantProperties.getIgnoreTables().stream().anyMatch(
                        (e) -> e.equalsIgnoreCase(tableName)
                );
            }
        };
    }

    /**
     * 过滤不需要根据租户隔离的MappedStatement
     */
    @Bean
    @ConditionalOnProperty(prefix = CommonConstants.TENANT_PREFIX, name = "type", havingValue = "tb")
    public ISqlParserFilter sqlParserFilter() {
        return metaObject -> {
            MappedStatement ms = SqlParserHelper.getMappedStatement(metaObject);
            return tenantProperties.getIgnoreSqls().stream().anyMatch(
                    (e) -> e.equalsIgnoreCase(ms.getId())
            );
        };
    }

    /**
     * 分页插件，自动识别数据库类型
     */
    @Bean
    @ConditionalOnBean({TenantHandler.class, ISqlParserFilter.class})
    @ConditionalOnProperty(prefix = CommonConstants.TENANT_PREFIX, name = "type", havingValue = "tb")
    public PaginationInterceptor paginationInterceptor( ISqlParserFilter sqlParserFilter) {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        boolean enableTenant = tenantProperties.getEnable();
        String type = tenantProperties.getType();
        //是否开启table多租户隔离
        if (enableTenant && "tb".equals(type)) {
            TenantSqlParser tenantSqlParser = new TenantSqlParser()
                    .setTenantHandler(tenantHandler());
            paginationInterceptor.setSqlParserList(CollUtil.toList(tenantSqlParser));
            paginationInterceptor.setSqlParserFilter(sqlParserFilter);
        } else {
            List<ISqlParser> sqlParserList = new ArrayList<>();
            // 攻击 SQL 阻断解析器、加入解析链
            sqlParserList.add(new BlockAttackSqlParser());
            paginationInterceptor.setSqlParserList(sqlParserList);
        }
        return paginationInterceptor;
    }
    ///**********************字段级隔离配置*******************/////////////


    ///**********************动态数据源相关*******************/////////////

    /**
     * 配置文件yml中的默认数据源
     *
     * @return
     */
    @Bean(name = "defaultDataSource")
    @ConditionalOnProperty(prefix = CommonConstants.TENANT_PREFIX, name = "type", havingValue = "db")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource getDefaultDataSource() {
        log.info("-----init default DataSource------");
        //return DataSourceBuilder.create().build();
        return DataSourceBuilder.create(dataSourceProperties.getClassLoader())
                .type(HikariDataSource.class)
                .driverClassName(dataSourceProperties.determineDriverClassName())
                .url(dataSourceProperties.determineUrl())
                .username(dataSourceProperties.determineUsername())
                .password(dataSourceProperties.determinePassword())
                .build();
    }

    /**
     * 初始化数据源，并用配置文件yml中的配置作为默认数据源
     * 只初始化一次
     *
     * @return
     */
    @Bean("dynamicDataSource")
    @ConditionalOnProperty(prefix = CommonConstants.TENANT_PREFIX, name = "type", havingValue = "db")
    public DataSource dynamicDataSource() {
        Map<Object, Object> dataSourceMap = new HashMap<>();
        HikariDataSource defaultDataSource = (HikariDataSource) getDefaultDataSource();
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
        log.info("-----init default DynamicDataSource {}------", defaultDataSource.getDataSource());
        return new DynamicDataSource(defaultDataSource, dataSourceMap);
    }

    //MP自带一个sqlSessionFactory配置，用我们这个作为主配置
    @Bean
    @Primary
    @ConditionalOnProperty(prefix = CommonConstants.TENANT_PREFIX, name = "type", havingValue = "db")
    //@ConditionalOnProperty(prefix = CommonConstants.DYNAMIC_DATASOURCE_PREFIX, name = "enabled", havingValue = "true")
    public MybatisSqlSessionFactoryBean sqlSessionFactoryBean() throws Exception {
        MybatisSqlSessionFactoryBean sessionFactory = new MybatisSqlSessionFactoryBean();
        /**
         * 重点，使分页插件生效
         */
        Interceptor[] plugins = new Interceptor[1];
        plugins[0] = new PaginationInterceptor();
        sessionFactory.setPlugins(plugins);
        //配置数据源，此处配置为关键配置，如果没有将 dynamicDataSource作为数据源则不能实现切换
        sessionFactory.setDataSource(dynamicDataSource());
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        // 扫描映射文件
        sessionFactory.setMapperLocations(resolver.getResources("classpath*:mapper/*/*Mapper.xml"));
        return sessionFactory;
    }

    @Bean
    @ConditionalOnProperty(prefix = CommonConstants.TENANT_PREFIX, name = "type", havingValue = "db")
    public PlatformTransactionManager transactionManager() {
        // 配置事务管理, 使用事务时在方法头部添加@Transactional注解即可
        return new DataSourceTransactionManager(dynamicDataSource());
    }
    ///**********************动态数据源相关*******************/////////////

}
