package com.redis.my_test;

import lombok.Data;

/**
 * @author wangyh
 * @create 2020-08-28 16:25
 */
@Data
public class Person {
    private String name;
    private Integer age;
    private Car car;
}
