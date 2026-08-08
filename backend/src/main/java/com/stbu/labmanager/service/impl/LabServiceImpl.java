package com.stbu.labmanager.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stbu.labmanager.entity.Lab;
import com.stbu.labmanager.mapper.LabMapper;
import com.stbu.labmanager.service.LabService;
import org.springframework.stereotype.Service;

@Service
public class LabServiceImpl extends ServiceImpl<LabMapper, Lab> implements LabService {
}