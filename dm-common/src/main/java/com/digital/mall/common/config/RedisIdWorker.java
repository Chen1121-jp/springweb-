package com.digital.mall.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * 基于 Redis 的全局唯一 ID 生成器
 * 使用时间戳 + 自增序列号拼接（类似雪花算法变种）
 */
@Configuration
public class RedisIdWorker {

    /**
     * 开始时间戳（2022-01-01 00:00:00 UTC）
     */
    private static final long BEGIN_TIMESTAMP = 1640995200L;

    /**
     * 序列号的位数
     */
    private static final int COUNT_BITS = 32;

    @Bean
    public RedisIdWorker redisIdWorker(StringRedisTemplate stringRedisTemplate) {
        return new RedisIdWorker(stringRedisTemplate);
    }

    private StringRedisTemplate stringRedisTemplate;

    public RedisIdWorker() {
    }

    public RedisIdWorker(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 生成下一个全局唯一 ID
     *
     * @param keyPrefix 业务前缀（如 "order"、"seckill"）
     * @return 全局唯一 ID
     */
    public long nextId(String keyPrefix) {
        // 1. 生成时间戳
        LocalDateTime now = LocalDateTime.now();
        long nowSecond = now.toEpochSecond(ZoneOffset.UTC);
        long timestamp = nowSecond - BEGIN_TIMESTAMP;

        // 2. 生成序列号（按天分组，避免单 key 过大）
        String date = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        long count = stringRedisTemplate.opsForValue().increment("icr:" + keyPrefix + ":" + date);

        // 3. 拼接并返回（时间戳 << 32 | 序列号）
        return timestamp << COUNT_BITS | count;
    }
}
