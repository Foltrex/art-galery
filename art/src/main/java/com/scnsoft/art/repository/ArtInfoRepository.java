package com.scnsoft.art.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scnsoft.art.entity.ArtInfo;

public interface ArtInfoRepository extends JpaRepository<ArtInfo, UUID> {
}
