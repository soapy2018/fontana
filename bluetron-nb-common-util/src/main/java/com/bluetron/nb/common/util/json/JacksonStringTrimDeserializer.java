package com.bluetron.nb.common.util.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * Description:
 *
 * @author genx
 * @date 2021/4/23 15:31
 */
public class JacksonStringTrimDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        //调用原始的字符串序列化
        String value = StringDeserializer.instance.deserialize(jsonParser, deserializationContext);
        if (value != null) {
            value = value.trim();
        }
        //去除空格后 如果为空字符串 则返回 null
        return StringUtils.isNotBlank(value) ? value : null;
    }
}
