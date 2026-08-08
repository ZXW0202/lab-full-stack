package com.stbu.labmanager;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.stbu.labmanager.mapper")
public class LabManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(LabManagerApplication.class, args);
    }
}