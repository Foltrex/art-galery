package com.scnsoft.user.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "account_metadata")
@Data
@Builder
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@FieldNameConstants
public class Metadata {

    @EmbeddedId
    private MetadataId metadataId;

    @NotEmpty
    private String value;

    public String toString() {
        return "Metadata[" + (metadataId != null ? metadataId.getKey() : "") + ":" + value + "]";
    }
}
