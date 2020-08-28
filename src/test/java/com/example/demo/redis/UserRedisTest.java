package com.example.demo.redis;

import com.example.demo.common.RedisUtil;
import com.example.demo.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.*;

/**
 * 手动使用缓存
 */
@SpringBootTest
public class UserRedisTest {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testStrSet() {
        User user = new User(1, "stringRedisTemp1", "stringRedisTemp1");
        stringRedisTemplate.opsForValue().set("stringRedisTemp", user.toString());

        User user2 = new User(2, "stringRedisTemp2", "stringRedisTemp2");
        ValueOperations<String, User> operations = redisTemplate.opsForValue();
        operations.set("redisTemp", user2);
    }

    @Test
    public void testStrGet() {
        Set<String> keys = redisTemplate.keys("*");

        String str = stringRedisTemplate.opsForValue().get("stringRedisTemp");
        System.out.println(str);

        User user = (User) redisTemplate.opsForValue().get("redisTemp");
        System.out.println();

    }

    @Test
    public void testHashSet() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", "3");
        map.put("username", "hashRedisTemp1");
        map.put("password", "hashRedisTemp1");
        HashOperations operations = redisTemplate.opsForHash();
        operations.putAll("hashRedisTemp", map);
    }

    @Test
    public void testHashGet() {
        //查询单个元素
        Object obj = redisTemplate.opsForHash().get("hashRedisTemp", "username");
        System.out.println(obj.toString());

        //修改单个元素的值
        redisTemplate.opsForHash().put("hashRedisTemp", "username", "update" );

        //批量查询
        List<String> fields = new ArrayList<String>();
        fields.add("id");
        fields.add("username");
        fields.add("password");
        List<String> values = redisTemplate.opsForHash().multiGet("hashRedisTemp",fields);
        System.out.println(values.toString());
    }

    @Test
    public void testDel() {
        boolean result = redisTemplate.delete("stringRedisTemp");
        System.out.println(result);

        List<String> keys = new ArrayList<String>();
        keys.add("hashRedisTemp");
        keys.add("redisTemp");
        keys.add("test");
        Long allResult = redisTemplate.delete(keys);
        System.out.println(allResult);
    }

    @Test
    public void testStrSet2() {
        RedisUtil.put("hashRedisTemp", 0);
        RedisUtil.put("redisTemp", 1);
        RedisUtil.put("test", 2);
    }

    @Test
    public void testDel2() {
        RedisUtil.delGroup("test");
    }
}
