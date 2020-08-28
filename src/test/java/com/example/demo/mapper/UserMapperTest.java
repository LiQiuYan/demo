package com.example.demo.mapper;

import com.example.demo.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
public class UserMapperTest {

    @Resource
    private UserMapper userMapper;

    @Test
    public void testQuery() throws Exception {
        List<User> user = userMapper.getAll();
        System.out.println(user.toString());
    }

    @Test
    public void testInsert() throws Exception {
        userMapper.insert(new User(null,"addtest1","addtest2"));
        System.out.println("count=====" + userMapper.getAll().size());
    }
}
