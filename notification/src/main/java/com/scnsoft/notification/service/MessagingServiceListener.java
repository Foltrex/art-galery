package com.scnsoft.notification.service;

import com.scnsoft.notification.config.MessagingConfig;
import com.scnsoft.notification.payload.NotificationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessagingServiceListener {

    private final MyMailSenderService myMailSenderService;

    @RabbitListener(queues = MessagingConfig.QUEUE)
    public void consumeMessageFromQueue(NotificationRequest notificationRequest) {
        try {
            System.out.println("Message from queue:");
            System.out.println(notificationRequest);

            myMailSenderService.sendMailMessage(
                    notificationRequest.getEmailTo(),
                    notificationRequest.getSubject(),
                    notificationRequest.getMessage()
            );
        } catch (Exception e) {
            log.error("Can't handle message!");
        }
    }

}
