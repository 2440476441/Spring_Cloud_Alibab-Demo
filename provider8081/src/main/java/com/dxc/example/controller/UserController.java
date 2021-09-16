package com.dxc.example.controller;

import com.dxc.example.mapper.UserMapper;
import com.dxc.example.service.UserService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Resource
    private UserMapper userMapper;
    @GetMapping("/get-user")
    public String getUser(){
        return "微服务8081";
    }

    @GetMapping("/del")
    @GlobalTransactional
    public String deleteUser(){
        userService.delUser();
        int i = userMapper.deleteById("1");
        return i==1?"删除成功":"删除失败";
    }
}