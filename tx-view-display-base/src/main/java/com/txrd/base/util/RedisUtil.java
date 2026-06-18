package com.txrd.base.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 设置缓存
     * @param key 键
     * @param value 值
     * @param time 过期时间 (秒)
     */
    public void set(String key, Object value, long time) {
        if (time > 0) {
            redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
        } else {
            redisTemplate.opsForValue().set(key, value);
        }
    }

    /**
     * 获取缓存
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除缓存
     */
    public Boolean del(String key) {
        return key != null && redisTemplate.delete(key);
    }

    /**
     * Hash 操作: 设置
     */
    public void hSet(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * Hash 操作: 获取
     */
    public Object hGet(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * Hash 操作: 获取所有
     */
    public java.util.Map<Object, Object> hGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 删除指定 Key 下，所有以 prefix 开头的 Hash Field
     * @param key Redis Key
     * @param fieldPrefix Field 的前缀，例如 "/system/"
     */
    public void deleteHashFieldsByPrefix(String key, String fieldPrefix) {
        // 1. 获取该 Key 下所有的 Field (注意：如果 Hash 很大，keys() 会阻塞，生产环境慎用)
        Set<Object> allFields = redisTemplate.opsForHash().keys(key);

        if (allFields == null || allFields.isEmpty()) {
            return;
        }

        // 2. 过滤出符合前缀的 Field
        Set<Object> fieldsToDelete = allFields.stream()
                .filter(field -> field instanceof String && ((String) field).startsWith(fieldPrefix))
                .collect(Collectors.toSet());

        // 3. 批量删除
        if (!fieldsToDelete.isEmpty()) {
            // 将 Object 集合转为 String 数组或 Object 数组
            Object[] fieldArray = fieldsToDelete.toArray();
            redisTemplate.opsForHash().delete(key, fieldArray);
        }
    }
}
