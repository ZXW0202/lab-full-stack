package com.stbu.labmanager.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stbu.labmanager.entity.Equipment;
import com.stbu.labmanager.mapper.EquipmentMapper;
import com.stbu.labmanager.service.EquipmentService;
import org.springframework.stereotype.Service;

@Service
public class EquipmentServiceImpl extends ServiceImpl<EquipmentMapper, Equipment> implements EquipmentService {
}