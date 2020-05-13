package com.sale_system.redis;

import com.alibaba.fastjson.JSON;
import com.sale_system.po.User;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: Sale_System
 * @description: Redis客户端
 * @author: buck
 * @create: 2020-05-13 21:48
 **/
public class RedisClient {
    private Jedis jedis;//非切片额客户端连接
    private JedisPool jedisPool;//非切片连接池
    private ShardedJedis shardedJedis;//切片额客户端连接
    private ShardedJedisPool shardedJedisPool;//切片连接池

    public RedisClient()
    {
        initialPool();
        initialShardedPool();
        shardedJedis = shardedJedisPool.getResource();
        jedis = jedisPool.getResource();
    }

    public Jedis getJedis(){
        return jedis;
    }
    /**
     * 初始化非切片池
     */
    private void initialPool()
    {
        // 池基本配置
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(5);
        config.setTestOnBorrow(false);
        jedisPool = new JedisPool(config,"192.168.177.131",22121);
    }

    /**
     * 初始化切片池
     */
    private void initialShardedPool()
    {
        // 池基本配置
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(5);
        config.setTestOnBorrow(false);
        // slave链接
        List<JedisShardInfo> shards = new ArrayList<>();
        shards.add(new JedisShardInfo("192.168.177.131", 22121, "master"));

        // 构造池
        shardedJedisPool = new ShardedJedisPool(config, shards);
    }

    public void show() {
        ObjectOperate() ;
    }

    private void ObjectOperate()
    {
        User user = new User("张三");
        String userStr = JSON.toJSONString(user);
        System.out.println(JSON.toJSONString(user));
        jedis.set("test", JSON.toJSONString(user));
        String value = jedis.get("test");
        System.out.println(value);
    }
}
