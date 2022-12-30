package com.scnsoft.art.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArtInfoDto {
    private UUID proposalId;
    private ArtDto artDto;
    private BigDecimal price;
    private Date creationDate;
    private Date expositionSateStart;
    private Date expositionDateEnd;
    private String status;
}
