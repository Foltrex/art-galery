package com.scnsoft.art.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scnsoft.art.entity.Art;

public interface ArtRepository extends JpaRepository<Art, UUID> {
    
}
