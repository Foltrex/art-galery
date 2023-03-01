package com.scnsoft.art.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.scnsoft.art.entity.ArtInfo;
import com.scnsoft.art.entity.Organization;

public interface ArtInfoRepository extends JpaRepository<ArtInfo, UUID> {
    List<ArtInfo> findByOrganization(Organization organization);

    @Query("""
        SELECT ai 
        FROM ArtInfo ai 
        JOIN ai.art a 
        WHERE a.id = :artId 
        AND ai.expositionDateEnd IS NULL        
    """)
    Optional<ArtInfo> findByArtId(@Param("artId") UUID artId);
}
