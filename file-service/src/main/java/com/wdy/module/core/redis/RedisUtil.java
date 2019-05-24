package com.wdy.module.core.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.concurrent.TimeUnit;

@Component("RedisUtil")
public class RedisUtil {
    //操作字符串的template，StringRedisTemplate是RedisTemplate的一个子集
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public Object sentinelGet(String key, Class clazz) {
        String value = this.stringRedisTemplate.opsForValue().get(key);
        return JSON.parseObject(value, clazz);
    }

    public boolean sentinelSet(final String key, Object value, Long expireTime) {
        boolean result;
        ValueOperations<String, String> valueOperations = this.stringRedisTemplate.opsForValue();
        valueOperations.set(key, JSON.toJSONString(value));
        stringRedisTemplate.expire(key, expireTime, TimeUnit.MILLISECONDS);
        result = true;
        return result;
    }

    public boolean sentinelSet(final String key, Object value) {
        boolean result;
        ValueOperations<String, String> valueOperations = this.stringRedisTemplate.opsForValue();
        valueOperations.set(key, JSON.toJSONString(value));
        result = true;
        return result;
    }

    public boolean removeKey(final String key) {
        return this.stringRedisTemplate.delete(key);
    }

    public boolean isExist(String key) {
        return this.stringRedisTemplate.hasKey(key);
    }

}
