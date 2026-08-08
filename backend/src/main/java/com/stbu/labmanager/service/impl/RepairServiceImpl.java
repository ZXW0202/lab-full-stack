package com.stbu.labmanager.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stbu.labmanager.entity.Repair;
import com.stbu.labmanager.mapper.RepairMapper;
import com.stbu.labmanager.service.RepairService;
import org.springframework.stereotype.Service;

@Service
public class RepairServiceImpl extends ServiceImpl<RepairMapper, Repair> implements RepairService {
}