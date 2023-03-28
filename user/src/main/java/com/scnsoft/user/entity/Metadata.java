package com.scnsoft.user.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.util.UUID;

@Entity
@Table(name = "account_metadata")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Metadata {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @NotEmpty
    private String key;

    @NotEmpty
    private String value;
}
