package com.scnsoft.art.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class City {
    @Id
    @GeneratedValue
    // @GeneratedValue(generator = "uuid2")
    // @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;
    private String name;
    private Double latitude;
    private Double longitude;
}
