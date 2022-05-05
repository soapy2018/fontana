package com.fontana.base.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.fontana.base.constant.CommonConstants;

/**
 * 租户holder
 *
 * @author cqf
 * @date 2021/10/5
 */
public class TenantContextHolder {

    public static final String DEFAULT_TENANT = CommonConstants.DEFAULT_TENANT;
    /**
     * 支持父子线程之间的数据传递
     */
    private static final ThreadLocal<String> CONTEXT = new TransmittableThreadLocal<String>() {
        @Override
        protected String initialValue() {
            return DEFAULT_TENANT;
        }
    };

    public static String getTenant() {
        return CONTEXT.get();
    }

    public static void setTenant(String tenant) {
        CONTEXT.set(tenant);
    }

    public static void clear() {
        CONTEXT.remove();
    }

    //设置默认租户
    public static void setDefaultTenant() {
        setTenant(TenantContextHolder.DEFAULT_TENANT);
    }
}
