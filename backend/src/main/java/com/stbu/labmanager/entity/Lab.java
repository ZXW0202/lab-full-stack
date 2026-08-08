package com.stbu.labmanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("lab")
public class Lab {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String labName;
    private String location;
    private String description;
}