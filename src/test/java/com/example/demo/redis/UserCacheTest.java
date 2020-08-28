package com.example.demo.redis;

import com.example.demo.domain.User;
import com.example.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * Springcache相关
 * 自动根据方法生成缓存
 */
@SpringBootTest
public class UserCacheTest {

    @Resource
    private UserService userService;

    @Test
    public void selectOneCacheTest() {
        User user = userService.getOne(5);
        System.out.println(user.toString());
    }

    @Test
    public void deleteCacheTest() {
        userService.delete(5);
    }
}
