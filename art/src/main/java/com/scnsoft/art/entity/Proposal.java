package com.scnsoft.art.entity;

import java.math.BigDecimal;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    
    @NotAudited
    @NotNull
    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;

    @NotAudited
    @NotNull
    @ManyToOne
    @JoinColumn(name = "artist_id", updatable = false, nullable = false)
    private Artist artist;

    @NotAudited
    @NotNull
    @ManyToOne
    @JoinColumn(name = "organization_id", updatable = false, nullable = false)
    private Organization organization;

    @NotAudited
    @ManyToOne
    @JoinColumn(name = "facility_id")
    private Facility facility;

    @NotAudited
    @ManyToOne
    @JoinColumn(name = "art_id")
    private Art art;

    private Boolean artistConfirmation;
    private Boolean organizationConfirmation;
}
