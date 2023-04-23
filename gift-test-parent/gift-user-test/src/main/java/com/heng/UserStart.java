package com.heng;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Hello world!
 */
@SpringBootApplication
@MapperScan("com.heng.mapper")
public class UserStart {
    public static void main(String[] args) {
        SpringApplication.run(UserStart.class,args);
    }
}
