package com.scnsoft.art;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableEurekaClient
@SpringBootApplication
@EnableFeignClients(basePackages = "com.scnsoft.art.feignclient")
public class ArtApplication {
    public static void main(String[] args) {
        SpringApplication.run(ArtApplication.class, args);
    }
}
