/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.bluetron.nb.common.util.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.TimeZone;

/**
 * 简单封装Jackson，实现JSON String<->Java Object的Mapper.
 * 封装不同的输出风格, 使用不同的builder函数创建实例.
 */
@Slf4j
public class JacksonMapperUtil extends ObjectMapper {

    private static final long serialVersionUID = 1L;

    private static final String PARSE_MSG = "parse json string error: ";

    private JacksonMapperUtil() {
        // 为Null时不序列化
        // this.setSerializationInclusion(Include.NON_NULL);
        // 允许单引号
        this.configure(Feature.ALLOW_SINGLE_QUOTES, true);
        // 允许不带引号的字段名称
        this.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // 加入输出排序
        this.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
        // 设置时区
        this.setTimeZone(TimeZone.getDefault());
        // 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        this.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        // 禁用空对象转换json校验
        this.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 遇到空值处理为空串
        this.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator jgen,
                                  SerializerProvider provider) throws IOException, JsonProcessingException {
                jgen.writeString("");
            }
        });

    }

    /**
     * 获取当前实例
     */
    public static JacksonMapperUtil getInstance() {
        return JsonMapperHolder.INSTANCE;
    }

    /**
     * 对象转换为JSON字符串
     */
    public static String toJson(Object object) {
        return JacksonMapperUtil.getInstance().toJsonString(object);
    }

    /**
     * JSON字符串转换为对象
     */
    public static <T> T fromJson(String jsonString, Class<T> clazz) {
        return JacksonMapperUtil.getInstance().fromJsonString(jsonString, clazz);
    }

    /**
     * JSON字符串转换复杂对象
     */
    public static <T> T fromJson(String jsonString, TypeReference<T> valueTypeRef) {
        return JacksonMapperUtil.getInstance().fromJsonString(jsonString, valueTypeRef);
    }

    /**
     * Object可以是POJO，也可以是Collection或数组。
     * 如果对象为Null, 返回"null".
     * 如果集合为空集合, 返回"[]".
     */
    public String toJsonString(Object object) {
        try {
            return this.writeValueAsString(object);
        } catch (IOException e) {
            log.warn("write to json string error: " + object, e);
            return null;
        }
    }

    /**
     * 反序列化POJO或简单Collection如List<String>.
     * 如果JSON字符串为Null或"null"字符串, 返回Null.
     * 如果JSON字符串为"[]", 返回空集合.
     * 如需反序列化复杂Collection如List<MyBean>, 请使用fromJson(String,JavaType)
     *
     * @see #fromJson(String, TypeReference<T> )
     */
    private <T> T fromJsonString(String jsonString, Class<T> clazz) {
        if (StringUtils.isEmpty(jsonString) || "<CLOB>".equals(jsonString)) {
            return null;
        }
        try {
            return this.readValue(jsonString, clazz);
        } catch (IOException e) {
            log.error(PARSE_MSG + jsonString, e);
            return null;
        }
    }

    /**
     * 确认泛型的转换
     *
     * @param <T>
     * @param jsonString
     * @param valueTypeRef
     * @return
     */
    private <T> T fromJsonString(String jsonString, TypeReference<T> valueTypeRef) {
        if (StringUtils.isEmpty(jsonString) || "<CLOB>".equals(jsonString)) {
            return null;
        }
        try {
            return this.readValue(jsonString, valueTypeRef);
        } catch (IOException e) {
            log.error(PARSE_MSG + jsonString, e);
            return null;
        }
    }

    /**
     * 当JSON里只含有Bean的部分属性時，更新一个已存在Bean，只覆盖该部分的属性.
     */
    @SuppressWarnings("unchecked")
    public <T> T update(String jsonString, T object) {
        try {
            return (T) this.readerForUpdating(object).readValue(jsonString);
        } catch (IOException e) {
            log.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
        }
        return null;
    }

    /**
     * 当前类的实例持有者（静态内部类，延迟加载，懒汉式，线程安全的单例模式）
     */
    private static final class JsonMapperHolder {
        private static final JacksonMapperUtil INSTANCE = new JacksonMapperUtil();
    }


}
