package com.bluetron.nb.common.db.config;

import com.bluetron.nb.common.base.constant.CommonConstants;
import com.bluetron.nb.common.base.context.TenantContextHolder;
import com.bluetron.nb.common.base.exception.GeneralException;
import com.bluetron.nb.common.db.cache.DataSourceCache;
import com.bluetron.nb.common.db.entity.TenantInfo;
import com.bluetron.nb.common.db.service.ITenantInfoService;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;
import java.util.HashMap;
import java.util.Map;

/**
 * @className: DynamicDataSource
 * @Description: 动态数据源实现类
 * @version: v1.0.0
 * @author: cqf
 * @date: 2021/11/4 14:45
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    /**
     * 用于保存租户key和数据源的映射关系，目标数据源map的拷贝
     */
    //public Map<Object, Object> backupTargetDataSources;
    @Autowired(required = false)
    private ITenantInfoService tenantInfoService;
    /**
     * 动态数据源构造器
     *
     * @param defaultDataSource 默认数据源
     * @param targetDataSource  目标数据源映射
     */
    public DynamicDataSource(DataSource defaultDataSource, Map<Object, Object> targetDataSource) {
        //backupTargetDataSources = targetDataSource;
        super.setDefaultTargetDataSource(defaultDataSource);
        // 存放数据源的map
        super.setTargetDataSources(targetDataSource);
        // afterPropertiesSet 的作用很重要，它负责解析成可用的目标数据源
        super.afterPropertiesSet();
    }

    /**
     * 必须实现其方法
     * 动态数据源类集成了Spring提供的AbstractRoutingDataSource类，AbstractRoutingDataSource
     * 中获取数据源的方法就是 determineTargetDataSource，而此方法又通过 determineCurrentLookupKey 方法获取查询数据源的key
     * 通过key在resolvedDataSources这个map中获取对应的数据源，resolvedDataSources的值是由afterPropertiesSet()这个方法从
     * TargetDataSources获取的
     *
     * @return
     */
    @Override
    protected Object determineCurrentLookupKey() {
        return TenantContextHolder.getTenant();
    }

//    @Override
//    protected Object determineCurrentLookupKey() {
//        String tenantId = TenantContextHolder.getTenant();
//        if(StringUtils.isNotEmpty(tenantId)){
//            if(!getDataSources().containsKey(tenantId)){
//                TenantInfo tenantInfo = tenantInfoService.getTenantInfo(tenantId);
//                if(null != tenantInfo){
//                    Map<Object, Object> dataSourceMap = new HashMap<>();
//                    HikariDataSource dataSource = new HikariDataSource();
//                    dataSource.setDriverClassName(tenantInfo.getDatasourceDriver());
//                    dataSource.setJdbcUrl(tenantInfo.getDatasourceUrl());
//                    dataSource.setUsername(tenantInfo.getDatasourceUsername());
//                    dataSource.setPassword(tenantInfo.getDatasourcePassword());
//                    dataSourceMap.putAll(getDataSources());
//                    dataSourceMap.put(tenantInfo.getTenantId(), dataSource);
//                    setDataSources(dataSourceMap);
//                }else{
//                    throw new GeneralException("租户不存在");
//                }
//            }
//            return tenantId;
//        }else {
//            return null;
//        }
//    }


    //父类利用此方法获取当前的DataSource，其实可以直接实现在此方法中，原则上要调用super()，这里直接整体覆盖了
    //用缓存维护数据源集合，同时若key过期自动触发删除缓存
    @Override
    protected DataSource determineTargetDataSource() {
        //获取数据源的key
        String tenantId = determineCurrentLookupKey().toString();
        //务必要上锁防止单个租户出现多个数据源
        synchronized (tenantId) {

            //default数据源不放入cache池，
            if(tenantId == null || CommonConstants.DEFAULT_TENANT.equals(tenantId)){
                return super.determineTargetDataSource();
            }
            //尝试从缓存中提取数据源
            HikariDataSource dataSource = (HikariDataSource) DataSourceCache.get(tenantId);
            if (null == dataSource) {
                TenantInfo tenantInfo = tenantInfoService.getTenantInfo(tenantId);
                if(null != tenantInfo){
                    Map<Object, Object> dataSourceMap = new HashMap<>();
                    HikariDataSource newDataSource = new HikariDataSource();
                    newDataSource.setDriverClassName(tenantInfo.getDatasourceDriver());
                    newDataSource.setJdbcUrl(tenantInfo.getDatasourceUrl());
                    newDataSource.setUsername(tenantInfo.getDatasourceUsername());
                    newDataSource.setPassword(tenantInfo.getDatasourcePassword());

                    try {
                        //手动定义的方式是没有完善的连接检查的，通过此种方式弥补，直接加载数据库连接
                        newDataSource.getConnection();
                    } catch (Exception e) {
                        //失败的处理请自行完善，例如重试 错误码
                        logger.error("数据库连接异常: {}", e);
                        throw new GeneralException("数据库连接异常");
                        //return null;
                    }
                    //添加缓存
                    DataSourceCache.set(tenantId, newDataSource);
                    return newDataSource;
                }else{
                    throw new GeneralException("租户不存在");
                }
            }
            return dataSource;
        }

    }


    /**
     * 设置数据源
     * @param dataSources
     */
    public void setDataSources(Map<Object, Object> dataSources) {
        super.setTargetDataSources(dataSources);
        super.afterPropertiesSet();
    }

    /**
     * 设置默认数据源
     * @param defaultDataSource
     */
    public void setDefaultDataSource(Object defaultDataSource) {
        super.setDefaultTargetDataSource(defaultDataSource);
    }

    /**
     * 获取全部数据源
     */
    public Map<Object, DataSource> getDataSources() {
        return super.getResolvedDataSources();
    }

}


