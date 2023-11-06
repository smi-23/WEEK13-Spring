package com.example.springfirstproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpringFirstProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringFirstProjectApplication.class, args);
    }

}
