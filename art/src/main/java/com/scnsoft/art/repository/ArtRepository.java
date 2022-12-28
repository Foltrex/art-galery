package com.scnsoft.art.repository;

import com.scnsoft.art.entity.Art;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ArtRepository extends JpaRepository<Art, UUID> {

}
