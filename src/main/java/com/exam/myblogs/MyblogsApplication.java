package com.exam.myblogs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MyblogsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyblogsApplication.class, args);
    }

}
