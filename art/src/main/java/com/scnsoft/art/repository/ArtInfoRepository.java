package com.scnsoft.art.repository;

import com.scnsoft.art.entity.ArtInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ArtInfoRepository extends JpaRepository<ArtInfo, UUID> {
}
