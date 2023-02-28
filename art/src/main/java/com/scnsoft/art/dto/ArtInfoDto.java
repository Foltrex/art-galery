package com.scnsoft.art.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArtInfoDto {
    private UUID id;
    private ArtDto art;
    private BigDecimal price;
    private FacilityDto facility;
    private OrganizationDto organization;
    private Double commission;
    private Date expositionDateStart;
    private Date expositionDateEnd;
    private String status;
}
