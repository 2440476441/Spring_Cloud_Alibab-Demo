package com.dxc.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dxc.example.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author 权计超
 * Company DXC.technology
 * @ClassName UserMapper
 * @CreateTime 2021-09-14 17:59
 * @Version 1.0
 * @Description: da
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
