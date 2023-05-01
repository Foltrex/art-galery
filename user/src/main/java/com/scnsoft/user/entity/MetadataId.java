package com.scnsoft.user.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@FieldNameConstants
@AllArgsConstructor
public class MetadataId implements Serializable {

    @Column(name = "account_id")
    private UUID accountId;

    @NotEmpty
    private String key;
}
