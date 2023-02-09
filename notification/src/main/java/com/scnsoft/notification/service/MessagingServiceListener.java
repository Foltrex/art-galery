package com.scnsoft.notification.service;

import com.scnsoft.notification.config.MessagingConfig;
import com.scnsoft.notification.payload.EmailMessagePayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessagingServiceListener {

    private final EmailSenderService emailSenderService;

    @RabbitListener(queues = MessagingConfig.QUEUE)
    public void consumeMessageFromQueue(EmailMessagePayload emailMessagePayload) {
        System.out.println(emailMessagePayload);
        try {
            emailSenderService.sendEmailMessage(emailMessagePayload);
        } catch (Exception e) {
            log.error("Can't handle message!");
        }
    }

}
