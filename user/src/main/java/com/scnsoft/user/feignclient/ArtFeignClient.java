package com.scnsoft.user.feignclient;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "art-service-arts", url = "http://localhost:8080/art-service/arts")
public interface ArtFeignClient {

    @DeleteMapping("/accounts/{id}")
    public void deleteByAccountId(@PathVariable("id") UUID id);
}
