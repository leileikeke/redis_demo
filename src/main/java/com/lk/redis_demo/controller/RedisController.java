package com.lk.redis_demo.controller;

import com.lk.redis_demo.bean.AppResponse;
import com.lk.redis_demo.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: leike
 * @date: 2019-10-29 18:57
 */
@RestController
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    private RedisService redisService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 模拟添加库存
     *
     * @return 一个统一返回体
     */
    @RequestMapping("/add")
    public AppResponse insertString() {
        stringRedisTemplate.opsForValue().set("stock", "50"); // 相当于jedis.set(key,value)
        return AppResponse.success("成功插入50条库存");
    }

    /**
     * 模拟分布式情境下的抢购
     *
     * @return
     */
    @RequestMapping("/deduct_stock")
    public String deductStock() {
        String lockKey = "product_001";
        String clientId = UUID.randomUUID().toString();
        try {
            // setIfAbsent相当于setnx(key,value): 只有在 key 不存在时设置 key 的值。存在则不设置并且返回false
            // API 提供了超时时间的设置
            Boolean result = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, clientId, 30, TimeUnit.SECONDS);

            if (!result) {
                return "error,繁忙,请稍后再试";
            }
            // 业务开始 start...
            // 查看当前库存
            int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));
            // 判断
            if (stock > 0) {
                int reslStock = stock - 1;
                stringRedisTemplate.opsForValue().set("stock", reslStock + "");
                System.out.println("扣减成功,剩余库存: " + reslStock + "");
            } else {
                System.out.println("库存不足");
            }
            // 业务结束 end
        } finally {
            // 判断如果当前的锁是客户端加的锁就删除锁
            if (stringRedisTemplate.opsForValue().get(lockKey).equals(clientId)) {
                stringRedisTemplate.delete(lockKey);
            }
        }
        return "end";
    }


}