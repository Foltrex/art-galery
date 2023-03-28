package com.scnsoft.art.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Currency {
    @Id
    @GeneratedValue
    private UUID id;

    private String value;
    private String label;
}
