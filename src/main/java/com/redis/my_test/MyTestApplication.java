package com.redis.my_test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class MyTestApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(MyTestApplication.class, args);
        RedisTest redisTest = ctx.getBean(RedisTest.class);
        redisTest.testRedis2();
    }

}
