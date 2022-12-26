package com.scnsoft.art.entity;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
@Builder
public class City {
    @Id
    private UUID id;
    private String name;
    private Double latitude;
    private Double longitude;
}
