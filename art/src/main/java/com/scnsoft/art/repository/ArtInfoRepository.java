package com.scnsoft.art.repository;

import com.scnsoft.art.entity.ArtInfo;
import com.scnsoft.art.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.List;

public interface ArtInfoRepository extends JpaRepository<ArtInfo, UUID> {
    List<ArtInfo> findByOrganization(Organization organization);
}
