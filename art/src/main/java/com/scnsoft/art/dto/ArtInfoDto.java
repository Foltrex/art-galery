package com.scnsoft.art.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import com.scnsoft.art.entity.Organization;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArtInfoDto {
    public enum Status {
        INACTIVE, ACTIVE, SOLD, RETURN
    }

    private UUID id;
    private UUID proposalId;
    private ArtDto artDto;
    private BigDecimal price;
    private Date expositionDateStart;
    private Date expositionDateEnd;
    private Status status;
}
