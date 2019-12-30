package com.sale_system.redis;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

public class BloomFilterTest {
    private static int total = 1000000;
    private static BloomFilter<Integer> bf = BloomFilter.create(Funnels.integerFunnel(),total);
    public static void main(String[] args) {
        for (int i=0;i<total;i++){
            bf.put(i);
        }
        for (int i=0;i<10000;i++){
            if(!bf.mightContain(i)){
                System.out.println(i);
            }
        }
        int count=0;
        for(int i=total;i<total+10000;i++){
            if(bf.mightContain(i)){
                count++;
            }
        }
        System.out.println("误报率："+count);
    }
}
