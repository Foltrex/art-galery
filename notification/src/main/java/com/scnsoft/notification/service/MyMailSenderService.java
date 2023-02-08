package com.scnsoft.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyMailSenderService {

    private final JavaMailSender mailSender;

    public void sendMailMessage(String emailTo, String subject, String message) {
        System.out.println("send message");
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("art.gallery.service@mail.ru");
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        try {
            mailSender.send(mailMessage);
            System.out.println("success");
        } catch (MailException e) {
            log.error("Error to send mail message: " + e);
        }
    }

}
