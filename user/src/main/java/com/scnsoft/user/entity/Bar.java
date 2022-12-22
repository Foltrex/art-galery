package com.scnsoft.user.entity;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Bar {
    private int maxArtsAmount;

    private List<UUID> arts;
}
