package com.scnsoft.notification.controller;

import com.scnsoft.notification.payload.EmailMessagePayload;
import com.scnsoft.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<Void> sendNotification(@RequestBody EmailMessagePayload emailMessagePayload) {
        notificationService.sendToQueue(emailMessagePayload);
        return ResponseEntity.ok().build();
    }

}
