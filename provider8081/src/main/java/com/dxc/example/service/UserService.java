package com.dxc.example.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Author 权计超
 * Company DXC.technology
 * @ClassName UserService
 * @CreateTime 2021-09-15 14:39
 * @Version 1.0
 * @Description: dww
 */
@FeignClient("provider02")
public interface UserService {
    @GetMapping("/user/del")
    public String delUser();
}