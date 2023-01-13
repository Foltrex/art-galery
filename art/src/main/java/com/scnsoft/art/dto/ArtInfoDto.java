package com.scnsoft.art.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class ArtInfoDto {
    public enum Status {
        INACTIVE, ACTIVE, SOLD, RETURN
    }

    private UUID id;
    private UUID proposalId;
    private ArtDto art;
    private BigDecimal price;
    private Date expositionDateStart;
    private Date expositionDateEnd;
    private Status status;
}
