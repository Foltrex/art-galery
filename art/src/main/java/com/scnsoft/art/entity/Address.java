package com.scnsoft.art.entity;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
public class Address {
    @Id
    @GeneratedValue
    private UUID id;
    private City city;
    private String streetName;
    private Integer streetNumber;
}
