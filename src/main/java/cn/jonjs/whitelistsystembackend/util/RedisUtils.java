package cn.jonjs.whitelistsystembackend.util;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

public class RedisUtils {

    public static void set(
            StringRedisTemplate stringRedisTemplate,
            String key,
            String value,
            long timeout,
            TimeUnit timeUnit
            ) {
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.set(key, value, timeout, timeUnit);
    }

    public static String get(StringRedisTemplate stringRedisTemplate, String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public static void delete(StringRedisTemplate stringRedisTemplate, String key) {
        stringRedisTemplate.opsForValue().getOperations().delete(key);
    }

}
