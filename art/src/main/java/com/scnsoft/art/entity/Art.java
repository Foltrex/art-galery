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
public class Art {
    @Id
    @GeneratedValue
    private UUID id;
    private String filename;
    private String description;
    private Artist artist;
}
