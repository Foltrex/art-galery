package com.scnsoft.art.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "entity_file")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntityFile {

    public enum Type {
        ORIGINAL,
        THUMBNAIL
    }

    @Id
    @GeneratedValue
    private UUID id;

    private UUID entityId;

    private UUID originalId;

    private Boolean isPrimary;

    @Enumerated(value = EnumType.ORDINAL)
    private Type type;

    @Column(name = "creation_date")
    @Builder.Default
    private Date creationDate = new Date();

}
