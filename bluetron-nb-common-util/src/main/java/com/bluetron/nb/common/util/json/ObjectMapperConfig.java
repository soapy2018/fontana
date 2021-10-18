package com.bluetron.nb.common.util.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 由于 {@link com.bluetron.app.autoconfigure.configure.ServiceConfiguration#objectMapper}
 * 导致 @JsonComponent 注解失效
 * 只能这样添加 自定义反序列化
 * {@link cn.bluetron.nb.autoparts.config.CustomWebMvcConfigure#configureMessageConverters(List)}
 * @author genx
 * @date 2021/4/26 12:16
 */
public class ObjectMapperConfig {

    private static final IntegerDeserializer INTEGER_DESERIALIZER = new IntegerDeserializer();

    private static final LongDeserializer LONG_DESERIALIZER = new LongDeserializer();

    public static void register(ObjectMapper objectMapper) {
        if (objectMapper != null) {
            Map<Class<?>, JsonDeserializer<?>> deserializerMap = new HashMap(8);
            deserializerMap.put(Integer.class, INTEGER_DESERIALIZER);
            deserializerMap.put(Long.class, LONG_DESERIALIZER);
            objectMapper.registerModule(new SimpleModule("bluetron", Version.unknownVersion(), deserializerMap));
        }
    }

    private static class IntegerDeserializer extends JsonDeserializer<Integer> {

        private final BigDecimal min = BigDecimal.valueOf(Integer.MIN_VALUE);
        private final BigDecimal max = BigDecimal.valueOf(Integer.MAX_VALUE);

        @Override
        public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            BigDecimal num;
            try {
                num = new BigDecimal(p.getValueAsString());
            } catch (Exception e) {
                throw new InvalidFormatException(p, "数字格式错误: " + p.getValueAsString(), p.getValueAsString(), Integer.class);
            }
            if (num.compareTo(max) > 0 || num.compareTo(min) < 0) {
                throw new InvalidFormatException(p, "超出数值范围: " + p.getValueAsString(), p.getValueAsString(), Integer.class);
            }
            if (num.stripTrailingZeros().scale() > 0) {
                throw new InvalidFormatException(p, "请不要输入小数: " + p.getValueAsString(), p.getValueAsString(), Integer.class);
            }
            return num.intValue();
        }
    }

    private static class LongDeserializer extends JsonDeserializer<Long> {

        private final BigDecimal min = BigDecimal.valueOf(Long.MIN_VALUE);
        private final BigDecimal max = BigDecimal.valueOf(Long.MAX_VALUE);

        @Override
        public Long deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            BigDecimal num;
            try {
                num = new BigDecimal(p.getValueAsString());
            } catch (Exception e) {
                throw new InvalidFormatException(p, "数字格式错误: " + p.getValueAsString(), p.getValueAsString(), Long.class);
            }
            if (num.compareTo(max) > 0 || num.compareTo(min) < 0) {
                throw new InvalidFormatException(p, "超出数值范围: " + p.getValueAsString(), p.getValueAsString(), Long.class);
            }
            if (num.stripTrailingZeros().scale() > 0) {
                throw new InvalidFormatException(p, "请不要输入小数: " + p.getValueAsString(), p.getValueAsString(), Long.class);
            }
            return num.longValue();
        }
    }
}
