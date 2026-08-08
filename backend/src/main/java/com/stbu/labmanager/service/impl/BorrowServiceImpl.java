package com.stbu.labmanager.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.stbu.labmanager.entity.Borrow;
import com.stbu.labmanager.mapper.BorrowMapper;
import com.stbu.labmanager.service.BorrowService;
import org.springframework.stereotype.Service;

@Service
public class BorrowServiceImpl extends ServiceImpl<BorrowMapper, Borrow> implements BorrowService {
}