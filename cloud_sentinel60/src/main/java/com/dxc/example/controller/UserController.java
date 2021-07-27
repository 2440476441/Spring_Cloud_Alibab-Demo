package com.dxc.example.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @RequestMapping("/get-user")
    @SentinelResource(value = "MyFlowRule",blockHandler = "Method02")
    public String getUSer(){
        return "老大爷好啊！活得好呀";
    }

    public String Method02(BlockException e){
        e.printStackTrace();
        return "系统繁忙，没空搭理你！";
    }
}