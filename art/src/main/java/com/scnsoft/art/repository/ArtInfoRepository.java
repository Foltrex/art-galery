package com.scnsoft.art.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scnsoft.art.entity.Art;
import com.scnsoft.art.entity.ArtInfo;
import com.scnsoft.art.entity.Organization;

public interface ArtInfoRepository extends JpaRepository<ArtInfo, UUID> {
    List<ArtInfo> findByOrganization(Organization organization);

    Optional<ArtInfo> findByArt(Art art);
}
