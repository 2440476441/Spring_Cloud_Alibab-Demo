package com.dxc.example.controller;

import com.dxc.example.mapper.UserMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserMapper userMapper;
    @GetMapping("/get-user")
    public String getUser(){
        return "微服务8082";
    }

    @GetMapping("/del")
    @Transactional
    public String deleteUser(){
        int i = userMapper.deleteById("1");
        return i==1?"删除成功":"删除失败";
    }
}