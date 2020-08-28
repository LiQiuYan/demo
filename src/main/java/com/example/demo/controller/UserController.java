package com.example.demo.controller;

import com.example.demo.domain.User;
import com.example.demo.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class UserController {

    @Resource
    private UserService userService;

    @RequestMapping("/getUsers")
    public List<User> getUsers() {
        List<User> users=userService.getAll();
        return users;
    }

    @PostMapping("/login")
    public String login(@RequestParam("name") String username, @RequestParam String password) {
        String str = "name:" + username + ",password:" + password;
        return str;
    }

    @GetMapping("/getUsers_page")
    public Object findPage(int pageNo, int pageSize){
        //设置分页信息
        PageHelper.startPage(pageNo, pageSize);
        List<User> users = userService.getAll();
        //生成分页信息对象
        PageInfo<User> pageInfo = new PageInfo<>(users);
        return pageInfo;
    }

}
