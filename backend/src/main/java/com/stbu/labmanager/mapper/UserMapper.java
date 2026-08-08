package com.stbu.labmanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.stbu.labmanager.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}