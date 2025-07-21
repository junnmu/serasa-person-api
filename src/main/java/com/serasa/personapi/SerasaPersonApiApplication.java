package com.serasa.personapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SerasaPersonApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(SerasaPersonApiApplication.class, args);
    }
}
