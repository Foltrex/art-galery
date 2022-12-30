package com.scnsoft.art.entity;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;

import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
@Audited
public class ArtInfo {
    public enum Status {
        INACTIVE, ACTIVE, SOLD, RETURN
    }

    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "art_id")
    private Art art;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "organisation_id")
    // @Column(updatable = false)
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "facility_id")
    // @Column(updatable = false)
    private Facility facility;

    @Column(updatable = false)
    private BigDecimal price;

    @Column(updatable = false)
    private double commission;

    @Column(updatable = false)
    private Date creationDate;

    private Date expositionDateStart;
    private Date expositionDateEnd;
    private Status status;
}
