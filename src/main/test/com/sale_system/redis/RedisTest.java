package com.sale_system.redis;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.sale_system.po.User;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class RedisTest {

    ApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
    RedisTemplate template = ctx.getBean(RedisTemplate.class);
    @Test
    public void testConnection(){

        RedisTemplate template = ctx.getBean(RedisTemplate.class);
        template.opsForValue().set("key1","asd");
        String value = template.opsForValue().get("key1").toString();
    }
    @Test
    public void testQueue(){
        try {

            Class cl = Class.forName("RedisTest");
            try {
                Object object = cl.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        for(int i = 0;i<1;i++){
            Thread thread = new Thread(new worker());
            thread.start();
        }
    }
    @Test
    public void testQueue2(){
        RedisTemplate template = ctx.getBean(RedisTemplate.class);
        int threadCount = 1;
        for(int i =0;i<threadCount;i++){
            Thread t = new Thread(new worker());
            t.start();
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(int i =0;i<1;i++){
            template.opsForList().leftPush("queue","message"+i);
        }
    }

    /**
     * 测试相同key名，value属于不同包或者不同项目的对象，序列化器：JdkSerializationRedisSerializer
     * 1.虽然不用指定类型也可以保存，但是在取数据的时候如果数据类型不符合就会报错
     */
    @Test
    public void testDiffObjeceSameKey(){
        User user1 = new User("s");
        com.sale_system.service.User user2 = new com.sale_system.service.User();
        user2.Name = "v";
        template.opsForValue().set("user",user1);
        template.opsForValue().set("user",user2);

        /*
         * 这里会报错，因为user这个key里面保存了两个不同类型的数据，这也是和Jackson2JsonRedisSerializer的区别之一。
         * 如果是后者，只要字段名相同就可以序列化出来。
         */
        User obj = (User)template.opsForValue().get("user");
    }

    /**
     * 测试StringRedisSerializer
     * 1.如果value是对象会直接报错
     */
    @Test
    public void testStringRedisSerializer(){
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        User user1 = new User("s");
        template.opsForValue().set("user",user1);
    }

    /**
     * 测试Jackson2JsonRedisSerializer
     */
    @Test
    public void testJackson2JsonRedisSerializer(){
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(User.class));
        User user1 = new User("s");
        template.opsForValue().set("jacksonUser",user1);

        /*
         * 列表类型数据不能正确反序列化出来，会报错，也就是说，Jackson2JsonRedisSerializer不兼容数组
         */
        List<User> users = Arrays.asList(new User("a"),new User("b"));
        template.opsForValue().set("users",users);
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(List.class));
        ValueOperations<String,List<User>> ops = template.opsForValue();
        List<User> users2 = ops.get("users");

        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(com.sale_system.service.User.class));
        /*
         * 这里可以正确得到user，所以这种序列化方式不关心redis里面的数据类型是什么，只要字段名称能对上就可以反序列化。
         * 所以它本质上就是json字符串到对象的转换。这种方式可以实现不同包不同项目之间的数据互通。
         */
        com.sale_system.service.User user = (com.sale_system.service.User)template.opsForValue().get("jacksonUser");
    }
    @Test
    public void testJackson2JsonRedisSerializer2(){
        template.setValueSerializer(new StringRedisSerializer());

        String value = (String)template.opsForValue().get("key1");
    }
    @Test
    /**
     * 管道测试
     */
    public void testPipeline(){
        template.executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {

                return null;
            }
        });
    }
    @Test
    public void testSortSet(){
        template.setValueSerializer(new StringRedisSerializer());
        template.setKeySerializer(new StringRedisSerializer());
        ZSetOperations ops = template.opsForZSet();
        for(int i=10;i<10000;i++){
            ops.add("userscore","yanxun"+i,i);
        }
    }
    @Test
    public void testGetSortSet(){
        template.setValueSerializer(new StringRedisSerializer());
        template.setKeySerializer(new StringRedisSerializer());
        ZSetOperations ops = template.opsForZSet();
        Set set = ops.range("userscore",0,1000);
    }


    public class worker implements Runnable{
        @Override
        public void run() {
            RedisTemplate template = ctx.getBean(RedisTemplate.class);
            while (true){
                Object obj = template.opsForList().rightPop("queue",0L,TimeUnit.SECONDS);
                System.out.println(obj.toString());
            }
        }
    }
}
