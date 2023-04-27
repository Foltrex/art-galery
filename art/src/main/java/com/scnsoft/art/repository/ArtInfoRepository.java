package com.scnsoft.art.repository;

import com.scnsoft.art.entity.ArtInfo;
import com.scnsoft.art.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ArtInfoRepository extends JpaRepository<ArtInfo, UUID> {
    List<ArtInfo> findByOrganization(Organization organization);

    @Query("""
                SELECT ai 
                FROM ArtInfo ai 
                JOIN ai.art a 
                WHERE a.id = :artId 
                AND ai.expositionDateEnd IS NULL        
            """)
    Optional<ArtInfo> findLastByArtId(@Param("artId") UUID artId);

    @Query("""
                SELECT ai 
                FROM ArtInfo ai 
                JOIN ai.art a 
                WHERE a.id = :artId
            """)
    List<ArtInfo> findByArtId(@Param("artId") UUID artId);
}
