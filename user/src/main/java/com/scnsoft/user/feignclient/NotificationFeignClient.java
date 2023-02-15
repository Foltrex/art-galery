package com.scnsoft.user.feignclient;

import com.scnsoft.user.payload.EmailMessagePayload;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "notification-service", url = "http://localhost:8085/notifications")
public interface NotificationFeignClient {

    @PostMapping("")
    ResponseEntity<Void> sendMessage(@RequestBody EmailMessagePayload emailMessagePayload);

}
