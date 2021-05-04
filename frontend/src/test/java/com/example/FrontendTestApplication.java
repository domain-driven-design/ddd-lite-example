package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class FrontendTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(FrontendTestApplication.class, args);
    }
}
