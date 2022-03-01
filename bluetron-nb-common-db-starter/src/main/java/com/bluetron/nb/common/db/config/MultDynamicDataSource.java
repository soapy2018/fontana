package com.bluetron.nb.common.db.config;

import com.bluetron.nb.common.base.context.DataSourceContextHolder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态数据源对象。当存在多个数据连接时使用。
 *
 * @author cqf
 * @date 2020-08-08
 */
public class MultDynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getDataSourceType();
    }
}
