package com.skyfree;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages="com.skyfree")
public class SkyBlogApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(SkyBlogApplication.class, args);
    }
}
