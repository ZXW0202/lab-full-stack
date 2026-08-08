package com.stbu.labmanager.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stbu.labmanager.entity.User;
import com.stbu.labmanager.mapper.UserMapper;
import com.stbu.labmanager.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}