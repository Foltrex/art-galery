package com.scnsoft.art.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;

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

    @NotNull
    @ManyToOne
    @JoinColumn(name = "artist_id", updatable = false)
    private Artist artist;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "organisation_id", updatable = false)
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "facility_id")
    private Facility facility;

    private Boolean artistConfirmation;
    private Boolean organisationConfirmation;
}
