package com.scnsoft.user.feignclient;

import com.scnsoft.user.dto.OrganizationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "organization-service", url = "http://localhost:8081/organizations")
public interface OrganizationFeignClient {

    @PostMapping()
    ResponseEntity<OrganizationDto> save(@RequestBody OrganizationDto organizationDto);

}
