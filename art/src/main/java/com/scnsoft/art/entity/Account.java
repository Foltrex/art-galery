package com.scnsoft.art.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.scnsoft.art.dto.AccountType;
import com.scnsoft.art.listener.AccountListener;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Builder
@FieldNameConstants
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AccountListener.class)
public class Account {

    public enum BlockReason {
        INVALID_PASSWORD
    }

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    private String firstName;

    private String lastName;

    @Column(unique = true)
    @Size(min = 3)
    private String email;

    @Size(min = 6)
    private String password;

    private Date lastFail;

    private Integer failCount;

    private Date blockedSince;
    private Long blockDuration;
    private BlockReason blockReason;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Builder.Default
    private Boolean isOneTimePassword = false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "accounts_m2m_roles",
            joinColumns = {@JoinColumn(name = "account_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    @JsonIgnore
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "metadataId.accountId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Metadata> metadata = new HashSet<>();

}
