package com.scnsoft.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.scnsoft.user.feignclient.FacilityFeignClient;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {
    @Autowired
    private FacilityFeignClient facilityFeignClient;

    @GetMapping
    public ResponseEntity<String> test() {
        log.info("this method was called");
        return facilityFeignClient.test();
    }
}
