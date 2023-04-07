package com.scnsoft.user.feignclient;

import java.util.UUID;

import org.mapstruct.Mapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.scnsoft.user.dto.OrganizationDto;

@FeignClient(value= "art-service-organization", url = "http://localhost:8080/art-service/organizations")
public interface OrganizationFeignClient {
    
    @GetMapping("/{id}")
    OrganizationDto findById(@PathVariable("id") UUID id);

    @GetMapping("/name")
    OrganizationDto findByName(@RequestParam("name") String name);

}
