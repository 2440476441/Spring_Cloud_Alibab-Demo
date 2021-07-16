package com.dxc.example.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("provider")
public interface UserService {
    @GetMapping("/user/get-user")
    String getUser();
}
