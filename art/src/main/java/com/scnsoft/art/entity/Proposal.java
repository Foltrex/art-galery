package com.scnsoft.art.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Audited
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Proposal {
    @Id
    @GeneratedValue
    private UUID id;
    private BigDecimal price;
    private double commission;
    private long currency;

    @NotAudited
    @NotNull
    @ManyToOne
    @JoinColumn(name = "artist_id", updatable = false, nullable = false)
    private Artist artist;

    @NotAudited
    @NotNull
    @ManyToOne
    @JoinColumn(name = "organisation_id", updatable = false, nullable = false)
    private Organization organization;

    @NotAudited
    @ManyToOne
    @JoinColumn(name = "facility_id")
    private Facility facility;

    private Boolean artistConfirmation;
    private Boolean organizationConfirmation;
}
