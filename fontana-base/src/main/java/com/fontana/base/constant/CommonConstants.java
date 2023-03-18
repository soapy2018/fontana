package com.fontana.base.constant;

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
    String APILOG_PREFIX = "fontana.apilog";

    /**
     * AUDIT log日志
     */
    String AUDITLOG_PREFIX = "fontana.auditlog";

    /**
     * TRACE log日志
     */
    String TRACELOG_PREFIX = "fontana.tracelog";

    /**
     * REQUEST log日志
     */
    String REQUESTLOG_PREFIX = "fontana.requestlog";

    /**
     * mybatis plus 配置
     */
    String MYBATISPLUS_AUTOFILL_PREFIX = "mybatis-plus.auto-fill";

    /**
     * redis配置
     */
    String CACHEMANAGER_PREFIX = "fontana.cache-manager";

    /**
     * tenant 配置
     */
    String TENANT_PREFIX = "fontana.tenant";

    /**
     * 动态数据源 配置
     */
    String DYNAMIC_DATASOURCE_PREFIX = "fontana.dynamic-datasource";

    /**
     * 锁配置
     */
    String LOCK_PREFIX = "fontana.lock";

    /**
     * xxlJob配置
     */
    String XXLJOB_PREFIX = "fontana.xxljob";

    /**
     * swagger2配置
     */
    String SWAGGER2_PREFIX = "fontana.swagger";

    /**
     * oss配置
     */
    String OSS_PREFIX = "fontana.oss";

    /**
     * datafilter配置
     */
    String DATAFILTER_PREFIX = "fontana.datafilter";

    /**
     * application配置
     */
    String APPLICATION_PREFIX = "fontana.application";

    /**
     * application配置
     */
    String SWAGGER_AGG_PREFIX = "fontana.swagger-agg";

    /**
     * online配置
     */
    String COMMON_ONLINE_PREFIX = "fontana.common-online";

    /**
     * flow配置
     */
    String COMMON_FLOW_PREFIX = "fontana.common-flow";

    /**
     * redisson配置
     */
    String REDISSON_PREFIX = "fontana.redisson";

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

    String BASE_CHECK_CODES = "qwertyuiplkjhgfdsazxcvbnmQWERTYUPLKJHGFDSAZXCVBNM1234567890";

    /**
     * 重要说明：该值为项目生成后的缺省密钥，仅为使用户可以快速上手并跑通流程。
     * 在实际的应用中，一定要为不同的项目或服务，自行生成公钥和私钥，并将 PRIVATE_KEY 的引用改为服务的配置项。
     * 密钥的生成方式，可通过执行common.core.util.RsaUtil类的main函数动态生成。
     */
    String PRIVATE_KEY =
            "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIX3oF33NkQgSpL3bL40+O4ccrncwUVoqDJ6m7yyc7hpOD3lGMbymO3mhIY6dqKAFJj88lTy7A4Rwom9lTGA0LtdZddB0GFwBFxdt4xJOrN6NNiB5tpRy4gY59RRlEKk17lALskTMXQaUUOxrkyESZTYI6mdr2jxRA2/NrCsEJfBAgMBAAECgYA/uFyq1R8WoFVYOh+bCQ8uEpHvHbVAEluORws0YZzxJfr+Va3HUzSyU2bNgZlUCZ/CdO2+whzNRF1Hrsj71s1w31VYUUKcCDWqsFomaBBhOnjPrGM4zK5YqYPSj8G1M8gyz6oSss4iG0w146CIp/3ogXuUBpHvDEDOXQijGgrgwQJBANRXGhfnBvS0KvMghEfLE0vwQvga0k/+Y8o7ETY00KJYZiw87+/jnXSIKgKey1n0GYdeiMN9UVwhAk082nvFTqUCQQChgzpVaeruzMTt2gXJVpVZg7yvhJKigrFVX00c+vMIRHgJCACmNFbh2WJZbv9FrUkgLyfdVUtFPzVhKjk+AlXtAkEAzWtqQd9tWkQGHOjDL9Uk2N0ZXH5OIbcfgN/9uawEfln4lilpkXBovfiQc7J9VYuROuVYd45/rBn+6QvK7dwmRQJAZyyWMgP0QNHsJuj0c1ZtF36xJQoyRb/pGllDwHSKlchvSY2kJIggzCt6yRi/1znIEdgtxhT7fWPQbJ51bKg/TQJAE1+ESOg8OQGy5hwbDUKGgHcqNpp3MbavQG7YYgEoZRzdskUtiBvsPyHxD3Eng/7vJcsv+Ncyy5HINc9wXUm4nw==";


    /**
     * 统计分类计算时，按天聚合计算的常量值。(前端在MyOrderParam和MyGroupParam中传给后台)
     */
    String DAY_AGGREGATION = "day";
    /**
     * 统计分类计算时，按月聚合计算的常量值。(前端在MyOrderParam和MyGroupParam中传给后台)
     */
    String MONTH_AGGREGATION = "month";
    /**
     * 统计分类计算时，按年聚合计算的常量值。(前端在MyOrderParam和MyGroupParam中传给后台)
     */
    String YEAR_AGGREGATION = "year";

    Integer WORK_NODE = 1;

    /**字典翻译文本后缀*/
   String DICT_TEXT_SUFFIX = "_dictText";

    /**
     * 删除标志
     */
    Integer DEL_FLAG_1 = 1;

    /**
     * 未删除
     */
   Integer DEL_FLAG_0 = 0;

    /**
     * 系统日志类型： 登录
     */
    int LOG_TYPE_1 = 1;

    /**
     * 系统日志类型： 操作
     */
    int LOG_TYPE_2 = 2;

    /**
     * 操作日志类型： 查询
     */
     int OPERATE_TYPE_1 = 1;

    /**
     * 操作日志类型： 添加
     */
    int OPERATE_TYPE_2 = 2;

    /**
     * 操作日志类型： 更新
     */
    int OPERATE_TYPE_3 = 3;

    /**
     * 操作日志类型： 删除
     */
    int OPERATE_TYPE_4 = 4;

    /**
     * 操作日志类型： 导入
     */
    int OPERATE_TYPE_5 = 5;

    /**
     * 操作日志类型： 导出
     */
    int OPERATE_TYPE_6 = 6;

    /**
     * 图片文件上传的父目录。
     */
    String UPLOAD_IMAGE_PARENT_PATH = "image";
    /**
     * 附件文件上传的父目录。
     */
    String UPLOAD_ATTACHMENT_PARENT_PATH = "attachment";
}


