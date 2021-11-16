package com.bluetron.nb.common.db.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

/**
 * @author chenqingfeng
 */
public class DataSourceCache {
    private static Cache<String, DataSource> cache;

    static {
        cache = CacheBuilder.newBuilder()
                //设置20分钟如果没有访问则过期 以免不活跃的连接耗费资源
                .expireAfterAccess(20, TimeUnit.MINUTES)
                //最大记录数
                .maximumSize(100000)
                //移除时关闭数据源
                .removalListener((RemovalListener<String, DataSource>) notification -> {
                    //清除和关闭数据源，不先执行close可能会导致不可预料的内存泄漏
                    HikariDataSource dataSource = (HikariDataSource) notification.getValue();
                    dataSource.close();
                })
                //还有很多配置可以自定义 尝试一下
                //可以指定cacheLoader把初始化数据源的代码写在build(cacheLoader)
                .build();
    }

    //当新增数据源时调用，以便于复用连接
    public static synchronized void set(String key, DataSource dataSource) {
        cache.put(key, dataSource);
    }

    //当获取数据源时调用，还未初始化会返回null
    public static synchronized DataSource get(String key) {
        return cache.getIfPresent(key);
    }

}