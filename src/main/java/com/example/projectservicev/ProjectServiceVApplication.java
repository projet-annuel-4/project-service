package com.example.projectservicev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients//("com.example.projectservicev")
public class ProjectServiceVApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectServiceVApplication.class, args);
    }

}