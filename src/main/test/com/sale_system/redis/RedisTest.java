package com.sale_system.redis;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisTest {

    @Test
    public void testConnection(){
        ApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
        RedisTemplate template = ctx.getBean(RedisTemplate.class);
        template.opsForValue().set("key1","asd");
        String value = template.opsForValue().get("key1").toString();
    }
}
