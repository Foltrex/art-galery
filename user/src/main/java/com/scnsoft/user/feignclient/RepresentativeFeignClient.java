package com.scnsoft.user.feignclient;

import com.scnsoft.user.dto.ArtistDto;
import com.scnsoft.user.dto.RepresentativeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(value = "representative-service", url = "http://localhost:8081/representatives")
public interface RepresentativeFeignClient {

    @PostMapping()
    ResponseEntity<RepresentativeDto> save(@RequestBody RepresentativeDto representativeDto);

}
