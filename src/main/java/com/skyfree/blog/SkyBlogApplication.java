package com.skyfree.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages="com.skyfree.blog")
public class SkyBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(SkyBlogApplication.class, args);
    }
}
