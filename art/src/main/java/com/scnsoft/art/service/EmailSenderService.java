package com.scnsoft.art.service;

import com.scnsoft.art.dto.EmailMessagePayload;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSenderService {

    private final JavaMailSender mailSender;
    private final Configuration configuration;

    @Value("${spring.mail.username}")
    private String sender;

    public void sendEmailMessage(EmailMessagePayload messagePayload) {
        MimeMessage message = prepareMessageForSending(messagePayload);

        try {
            mailSender.send(message);
            log.info("Send message successfully");
        } catch (MailException e) {
            log.error("Error to send mail message: " + e);
        }
    }

    private MimeMessage prepareMessageForSending(EmailMessagePayload messagePayload) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message);

            Template template = configuration.getTemplate(messagePayload.getTemplateFile().getName());
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, messagePayload.getProperties());

            helper.setFrom(messagePayload.getSender() != null ? messagePayload.getSender() : sender);
            helper.setTo(messagePayload.getReceiver());
            helper.setSubject(messagePayload.getSubject());
            helper.setText(html, true);
            helper.setSentDate(new Date());

        } catch (MessagingException | IOException | TemplateException e) {
            log.error("Error to create mail message: " + e);
        }

        return message;
    }

}