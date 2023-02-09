package com.scnsoft.user.payload;

import com.scnsoft.user.entity.constant.TemplateFile;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import java.util.Map;

@Data
@Builder
public class EmailMessagePayload {

    @Email
    private String sender;

    @Email
    private String receiver;

    private String subject;

    private TemplateFile templateFile;

    private Map<String, String> properties;

}
