package com.bluetron.nb.common.util.tools;

import cn.hutool.core.util.RandomUtil;
import org.slf4j.MDC;

/**
 * 日志追踪工具类
 *
 * @author cqf
 * @date 2021/10/14
 * <p>
 */
public class MDCTraceUtils {
    /**
     * 追踪id的名称
     */
    public static final String KEY_TRACE_ID = "traceId";

    /**
     * filter的优先级，值越低越优先
     */
    public static final int FILTER_ORDER = -1;

    /**
     * 创建traceId并赋值MDC
     */
    public static void addTrace() {
        String traceId = createTraceId();
        MDC.put(KEY_TRACE_ID, traceId);
    }

    /**
     * 赋值MDC
     */
    public static void putTrace(String traceId) {
        MDC.put(KEY_TRACE_ID, traceId);
    }

    /**
     * 获取MDC中的traceId值
     */
    public static String getTraceId() {
        return MDC.get(KEY_TRACE_ID);
    }

    /**
     * 清除MDC的值
     */
    public static void removeTrace() {
        MDC.remove(KEY_TRACE_ID);
    }

    /**
     * 创建traceId
     */
    public static String createTraceId() {
        return RandomUtil.randomString(16);
    }
}
