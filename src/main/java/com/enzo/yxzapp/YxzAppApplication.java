package com.enzo.yxzapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class YxzAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(YxzAppApplication.class, args);
    }

}
