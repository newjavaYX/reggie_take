package com.example;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@MapperScan("com.example.reggie.dao")
@ServletComponentScan
@SpringBootApplication
@EnableTransactionManagement
@EnableCaching
public class ReggieTakeApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReggieTakeApplication.class, args);
        log.info("项目启动成功");
    }

}
