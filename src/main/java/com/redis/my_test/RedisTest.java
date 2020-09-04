package com.redis.my_test;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.hash.BeanUtilsHashMapper;
import org.springframework.data.redis.hash.Jackson2HashMapper;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wangyh
 * @create 2020-08-28 16:15
 */
@Component
public class RedisTest {

    @Autowired
    public StringRedisTemplate redisTemplate;

    @Autowired
    @Qualifier("jsonRedis")
    public StringRedisTemplate jsonRedis;


    @Autowired
    ObjectMapper objectMapper;

    public void testRedis() {
        //redisTemplate.opsForValue().set("hello", "world");
        Person p = new Person();
        p.setName("张三");
        p.setAge(20);
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        //redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));
        Jackson2HashMapper jm = new Jackson2HashMapper(objectMapper, false);
        redisTemplate.opsForHash().putAll("sean01", jm.toHash(p));
    }


    public void testRedis2() {
        Car car = new Car();
        car.setPrice(100);
        car.setType("不知道");
        Person person = new Person();
        person.setAge(10);
        person.setName("张三");
        person.setCar(car);
        Jackson2HashMapper jm = new Jackson2HashMapper(objectMapper, false);
        redisTemplate.opsForHash().putAll("tom1", jm.toHash(person));
    }

    /**
     * 发布订阅模式
     */
    public void testRedsi2() {

    }

}
