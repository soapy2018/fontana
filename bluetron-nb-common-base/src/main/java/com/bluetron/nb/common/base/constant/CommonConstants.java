package com.bluetron.nb.common.base.constant;

/**
 * @className: HttpConstants
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2021/9/22 10:27
 */
public interface CommonConstants {

    /**
     * 国际化资源目录
     */
    String I18N_PATH_PARENT = "classpath*:i18n/**/*.properties";

    /**
     * UTF8编码
     */
    String UTF8 = "UTF-8";

    /**
     * 默认地区
     */
    String DEFAULT_LOCAL_NAME = "zh_CN";

    /**
     * 默认i18n文件
     */
    String DEFAULT_PROPERTY_FILE = "common.properties";

    /**
     * 默认文件名分隔符
     */
    String UNDERLINE = "_";

    /**
     * 点号分隔符
     */
    String DOT = ".";

    /**
     * API log日志
     */
    String APILOG_PREFIX = "bluetron-nb.common.apilog";

    /**
     * AUDIT log日志
     */
    String AUDITLOG_PREFIX = "bluetron-nb.common.auditlog";

    /**
     * mybatis plus 配置
     */
    String MYBATISPLUS_PREFIX = "bluetron-nb.common.mybatis-plus";

    /**
     * redis配置
     */
    String CACHEMANAGER_PREFIX = "bluetron-nb.common.cache-manager";

    /**
     * mybatis plus 配置
     */
    String TENANT_PREFIX = "bluetron-nb.common.tenant";

    /**
     * mybatis plus 配置
     */
    String DYNAMIC_DATASOURCE_PREFIX = "bluetron-nb.common.dynamic-datasource";

    /**
     * 锁配置
     */
    String LOCK_PREFIX = "bluetron-nb.common.lock";

    /**
     * xxlJob配置
     */
    String XXLJOB_PREFIX = "bluetron-nb.common.xxljob";

    /**
     * swagger2配置
     */
    String SWAGGER2_PREFIX = "bluetron-nb.common.swagger2";

    /**
     * 数据表中的tenant_id字段
     */
    String TENANT_ID_COLUMN = "tenant_id";

    /**
     * redis锁前缀
     */
    String LOCK_KEY_PREFIX = "LOCK_KEY";

    /**
     * redis缓存name
     */
    String TENANT_DATASOURCE_KEY = "tenantDataSource";

    /**
     * 默认租户id
     */
    String DEFAULT_TENANT = "default";

    /**
     * 公共日期格式
     */
    String MONTH_FORMAT = "yyyy-MM";
    String DATE_FORMAT = "yyyy-MM-dd";
    String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    String SIMPLE_MONTH_FORMAT = "yyyyMM";
    String SIMPLE_DATE_FORMAT = "yyyyMMdd";
    String SIMPLE_DATETIME_FORMAT = "yyyyMMddHHmmss";
    String TIME_ZONE_GMT8 = "GMT+8";

}


