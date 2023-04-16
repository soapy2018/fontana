package com.fontana.redis.properties;

import com.fontana.base.constant.CommonConstants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author cqf
 * @date 2021/11/6
 */
@Setter
@Getter
@ConfigurationProperties(prefix = CommonConstants.CACHEMANAGER_PREFIX)
public class CacheManagerProperties {

    private List<CacheConfig> configs;

    @Setter
    @Getter
    public static class CacheConfig {
        /**
         * cache key
         */
        private String key;
        /**
         * 过期时间，sec
         */
        private long second = 60;
    }
}
