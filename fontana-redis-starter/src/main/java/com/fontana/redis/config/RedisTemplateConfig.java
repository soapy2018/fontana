package com.fontana.redis.config;

import com.fontana.redis.util.RedisTemplateUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * RedisTemplate 配置类
 *
 * @author cqf
 * @date 2021/11/6 11:02
 * <p>
 */
@Configuration
public class RedisTemplateConfig {

    /**
     * RedisTemplate配置
     *
     * @param factory
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory
            , RedisSerializer<String> redisKeySerializer, RedisSerializer<Object> redisValueSerializer) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);

        redisTemplate.setDefaultSerializer(redisValueSerializer);
        redisTemplate.setKeySerializer(redisKeySerializer);
        redisTemplate.setHashKeySerializer(redisKeySerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean(name = "RedisTemplateUtil")
    @ConditionalOnBean({RedisTemplate.class})
    public RedisTemplateUtil redisTemplateUtil(RedisTemplate<String, Object> redisTemplate) {
        return new RedisTemplateUtil(redisTemplate);
    }

}
