package com.bloomfilter;

import redis.clients.jedis.Jedis;

public class RedisBitSetTest {
    public static void main(String[] args) {
        BloomFilter<String> bf = new BloomFilter<>(0.0001,10000);
        Jedis jedis = new Jedis("192.168.177.129",6379);
        bf.bind(new RedisBitSet(jedis,"bloomfilter:key:name"));
        bf.add("filter");
        System.out.println(bf.contains("filter"));
    }
}
