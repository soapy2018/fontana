package com.fontana.redis.util;

import com.fontana.util.request.WebContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import java.util.HashSet;
import java.util.Set;

/**
 * Session数据缓存辅助类。
 *
 * @author cqf
 * @date 2020-08-08
 */
@SuppressWarnings("unchecked")
public class SessionCacheHelper {

    private static final int DEFAULT_TTL = 3600000;

    @Autowired
    private CacheManager cacheManager;

    /**
     * 定义cache名称、超时时长(毫秒)。
     */
    public enum CacheEnum {
        /**
         * session下上传文件名的缓存(时间是24小时)。
         */
        UPLOAD_FILENAME_CACHE(86400000),
        /**
         * 缺省全局缓存(时间是24小时)。
         */
        GLOBAL_CACHE(86400000);

        /**
         * 缓存的时长(单位：毫秒)
         */
        private int ttl = DEFAULT_TTL;

        CacheEnum() {
        }

        CacheEnum(int ttl) {
            this.ttl = ttl;
        }

        public int getTtl() {
            return ttl;
        }
    }

    /**
     * 缓存当前session内，上传过的文件名。
     *
     * @param filename 通常是本地存储的文件名，而不是上传时的原始文件名。
     */
    public void putSessionUploadFile(String filename) {
        if (filename != null) {
            Set<String> sessionUploadFileSet = null;
            Cache cache = cacheManager.getCache(CacheEnum.UPLOAD_FILENAME_CACHE.name());
            Cache.ValueWrapper valueWrapper = cache.get(WebContextUtil.takeTokenFromRequest().getSessionId());
            if (valueWrapper != null) {
                sessionUploadFileSet = (Set<String>) valueWrapper.get();
            }
            if (sessionUploadFileSet == null) {
                sessionUploadFileSet = new HashSet<>();
            }
            sessionUploadFileSet.add(filename);
            cache.put(WebContextUtil.takeTokenFromRequest().getSessionId(), sessionUploadFileSet);
        }
    }

    /**
     * 判断参数中的文件名，是否有当前session上传。
     *
     * @param filename 通常是本地存储的文件名，而不是上传时的原始文件名。
     * @return true表示该文件是由当前session上传并存储在本地的，否则false。
     */
    public boolean existSessionUploadFile(String filename) {
        if (filename == null) {
            return false;
        }
        Cache cache = cacheManager.getCache(CacheEnum.UPLOAD_FILENAME_CACHE.name());
        Cache.ValueWrapper valueWrapper = cache.get(WebContextUtil.takeTokenFromRequest().getSessionId());
        if (valueWrapper == null) {
            return false;
        }
        return ((Set<String>) valueWrapper.get()).contains(filename);
    }

    /**
     * 清除当前session的所有缓存数据。
     *
     * @param sessionId 当前会话的SessionId。
     */
    public void removeAllSessionCache(String sessionId) {
        for (CacheEnum c : CacheEnum.values()) {
            cacheManager.getCache(c.name()).evict(sessionId);
        }
    }
}
