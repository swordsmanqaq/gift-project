package com.heng;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


/**
 * Hello world!
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.heng.mapper")
@EnableFeignClients
public class ShopCarServerStart {
    public static void main(String[] args) {
        SpringApplication.run(ShopCarServerStart.class, args);
    }
}
