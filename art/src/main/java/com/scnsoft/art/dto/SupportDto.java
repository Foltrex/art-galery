package com.scnsoft.art.dto;

import com.scnsoft.art.entity.SupportType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class SupportDto {
    private UUID id;
    private UUID accountId;
    private UUID threadId;
    private SupportType type;
    private String message;
    private Date createdAt;
}
