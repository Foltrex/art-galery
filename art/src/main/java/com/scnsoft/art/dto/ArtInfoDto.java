package com.scnsoft.art.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArtInfoDto {
    private UUID id;
    private UUID artId;
    private BigDecimal price;
    private CurrencyDto currency;
    private FacilityDto facility;
    private OrganizationDto organization;
    private Double commission;
    private Date expositionDateStart;
    private Date expositionDateEnd;
    private String status;

}
