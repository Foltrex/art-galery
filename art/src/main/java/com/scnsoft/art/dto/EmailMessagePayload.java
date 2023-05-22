package com.scnsoft.art.dto;

import com.scnsoft.art.entity.constant.TemplateFile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class EmailMessagePayload {

    @Email
    private String sender;

    @Email
    private String receiver;

    private String subject;

    private TemplateFile templateFile;

    private Map<String, String> properties = new HashMap<>();

}
