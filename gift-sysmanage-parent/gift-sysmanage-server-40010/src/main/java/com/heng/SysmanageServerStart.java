package com.heng;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Hello world!
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.heng.mapper")
public class SysmanageServerStart {
    public static void main(String[] args) {
        SpringApplication.run(SysmanageServerStart.class, args);
    }
}
