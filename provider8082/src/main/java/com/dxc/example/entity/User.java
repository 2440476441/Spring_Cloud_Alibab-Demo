package com.dxc.example.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author 权计超
 * Company DXC.technology
 * @ClassName User
 * @CreateTime 2021-09-14 17:53
 * @Version 1.0
 * @Description: 用户
 */
@Data
@TableName("user")
public class User {
    @TableId("id")
    private Integer id;
    @TableField("name")
    private String name;
}
