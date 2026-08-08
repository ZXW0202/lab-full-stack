package com.stbu.labmanager.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.stbu.labmanager.common.Result;
import com.stbu.labmanager.entity.Borrow;
import com.stbu.labmanager.entity.Equipment;
import com.stbu.labmanager.entity.Lab;
import com.stbu.labmanager.entity.Repair;
import com.stbu.labmanager.entity.User;
import com.stbu.labmanager.mapper.BorrowMapper;
import com.stbu.labmanager.mapper.EquipmentMapper;
import com.stbu.labmanager.mapper.LabMapper;
import com.stbu.labmanager.mapper.RepairMapper;
import com.stbu.labmanager.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private LabMapper labMapper;

    @Autowired
    private EquipmentMapper equipmentMapper;

    @Autowired
    private BorrowMapper borrowMapper;

    @Autowired
    private RepairMapper repairMapper;

    /**
     * 首页统计：用户数、实验室数、设备数、待借用、待维修
     */
    @GetMapping("/stats")
    public Result<Map<String, Object>> stats() {
        Map<String, Object> map = new HashMap<>();

        // 总数（count 不是 id）
        map.put("userCount", userMapper.selectCount(null));
        map.put("labCount", labMapper.selectCount(null));
        map.put("equipmentCount", equipmentMapper.selectCount(null));

        // 待处理借用：状态=借用中
        LambdaQueryWrapper<Borrow> borrowWrap = new LambdaQueryWrapper<>();
        borrowWrap.eq(Borrow::getStatus, "借用中");
        map.put("borrowPending", borrowMapper.selectCount(borrowWrap));

        // 待维修设备：状态=维修中
        LambdaQueryWrapper<Repair> repairWrap = new LambdaQueryWrapper<>();
        repairWrap.eq(Repair::getStatus, "维修中");
        map.put("equipmentRepair", repairMapper.selectCount(repairWrap));

        return Result.success(map);
    }
}