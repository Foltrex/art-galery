package com.scnsoft.notification.payload;

import com.scnsoft.notification.constant.TemplateFile;
import lombok.Data;

import javax.validation.constraints.Email;
import java.util.HashMap;
import java.util.Map;

@Data
public class EmailMessagePayload {

    @Email
    private String sender;

    @Email
    private String receiver;

    private String subject;

    private TemplateFile templateFile;

    private Map<String, String> properties = new HashMap<>();

}
