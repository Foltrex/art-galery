package com.scnsoft.art.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class ArtInfo {
    public enum Status {
        INACTIVE, ACTIVE, SOLD, RETURN
    }

    @Id
    @GeneratedValue
    private UUID id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "art_id")
    private Art art;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "facility_id")
    private Facility facility;

    @Column(updatable = false)
    private BigDecimal price;

    @Column(updatable = false)
    private double commission;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;

    private Date expositionDateStart;
    private Date expositionDateEnd;
    private Status status;
}
