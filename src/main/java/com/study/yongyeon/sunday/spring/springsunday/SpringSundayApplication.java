package com.study.yongyeon.sunday.spring.springsunday;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpringSundayApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringSundayApplication.class, args);
    }
}
