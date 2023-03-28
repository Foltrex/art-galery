package com.scnsoft.user.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Data
public class MetadataId implements Serializable {

    @Column(name = "account_id")
    private UUID accountId;

    @NotEmpty
    private String key;

}
