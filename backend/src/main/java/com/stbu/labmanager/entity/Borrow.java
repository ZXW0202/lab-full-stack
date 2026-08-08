package com.stbu.labmanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("borrow")
public class Borrow {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer eqId;
    private Integer userId;
    private LocalDateTime borrowTime;
    private LocalDateTime returnTime;
    private String status;
}