package com.redis.my_test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

/**
 * @author wangyh
 * @create 2020-08-29 16:59
 */
@Configuration
public class MyRedis {

    @Bean
    public StringRedisTemplate jsonRedis(RedisConnectionFactory fc) {
        StringRedisTemplate tp = new StringRedisTemplate(fc);
        tp.setHashValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));
        return tp;
    }
}
