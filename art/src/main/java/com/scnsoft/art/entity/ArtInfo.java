package com.scnsoft.art.entity;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@Table(name = "art_info")
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
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "facility_id")
    private Facility facility;

    private BigDecimal price;
    private double commission;

    private Date creationDate;
    private Date expositionDateStart;
    private Date expositionDateEnd;

    private Status status;
}
