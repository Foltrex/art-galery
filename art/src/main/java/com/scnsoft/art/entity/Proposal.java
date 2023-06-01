package com.scnsoft.art.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Audited
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldNameConstants
public class Proposal {
    public enum ProposalStatus {
        SENT, AWAIT, CANCELED, APPROVED
    }
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

    private UUID accountId;
    private ProposalStatus status;

    @NotAudited
    @NotNull
    @ManyToOne
    @JoinColumn(name = "organization_id", updatable = false, nullable = false)
    private Organization organization;

    @NotAudited
    @ManyToMany
    @JoinTable(
            name = "proposal_m2m_facility",
            joinColumns = {@JoinColumn(name = "proposal_id")},
            inverseJoinColumns = {@JoinColumn(name = "facility_id")}
    )
    private List<Facility> facilities;

    @NotAudited
    @ManyToOne
    @JoinColumn(name = "art_id")
    private Art art;

    private Boolean artistConfirmation;
    private Boolean organizationConfirmation;
}
