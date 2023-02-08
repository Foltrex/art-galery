package com.scnsoft.notification.payload;

import lombok.Data;

@Data
public class NotificationRequest {

    private String emailTo;
    private String subject;
    private String message;
}
