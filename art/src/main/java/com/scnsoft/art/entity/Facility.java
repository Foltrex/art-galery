package com.scnsoft.art.entity;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Facility {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private boolean isActive;
    private UUID cityId;
}
