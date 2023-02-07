package com.scnsoft.notification.service;

import com.scnsoft.notification.config.MessagingConfig;
import com.scnsoft.notification.payload.NotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final RabbitTemplate rabbitTemplate;

    public void sendToQueue(NotificationRequest notificationRequest) {
        rabbitTemplate.convertAndSend(
                MessagingConfig.EXCHANGE,
                MessagingConfig.KEY,
                notificationRequest);
    }

}
