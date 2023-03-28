package com.scnsoft.user.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "account_metadata")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Metadata {

    @EmbeddedId
    private MetadataId metadataId;

    @NotEmpty
    private String value;

}
