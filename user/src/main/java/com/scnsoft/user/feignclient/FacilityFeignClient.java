package com.scnsoft.user.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "facility-service", url = "http://localhost:8080/art-service/facilities")
public interface FacilityFeignClient {
    @GetMapping
    ResponseEntity<String> test();
}
