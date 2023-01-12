package com.scnsoft.art;

import com.scnsoft.art.entity.Organization;
import com.scnsoft.art.entity.OrganizationRole;
import com.scnsoft.art.entity.Representative;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.UUID;

@EnableEurekaClient
@SpringBootApplication
@EnableFeignClients(basePackages = "com.scnsoft.art.feignclient")
public class ArtApplication {
    public static void main(String[] args) {
        SpringApplication.run(ArtApplication.class, args);
    }
}
