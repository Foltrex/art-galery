package com.scnsoft.art.dto;

import com.scnsoft.art.entity.Support;
import com.scnsoft.art.entity.SupportThread;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.UUID;
@Getter
@Setter
public class SupportThreadDto {
    private UUID id;
    private UUID accountId;
    private String email;
    private String name;
    private String subject;
    private SupportThread.SupportThreadStatus status;
    private String message;
    private Date createdAt;
    private Date updatedAt;

    private List<FileInfoDto> files;

    private List<SupportDto> posts;
}
