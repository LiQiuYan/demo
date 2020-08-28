package com.example.demo.service;

import com.example.demo.domain.User;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
public class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    public void testFindPage(){
        int pageNo = 3;
        int pageSize = 2;

        //设置分页信息
        PageHelper.startPage(pageNo, pageSize);
        List<User> users = userService.getAll();
        //生成分页信息对象
        PageInfo<User> pageInfo = new PageInfo<>(users);
        System.out.println(pageInfo.toString());
    }

}
