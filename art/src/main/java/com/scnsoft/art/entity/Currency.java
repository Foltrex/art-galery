package com.scnsoft.art.entity;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Currency {
    @Id
    @GeneratedValue
    private UUID id;

    private String value;
    private String label;
}
