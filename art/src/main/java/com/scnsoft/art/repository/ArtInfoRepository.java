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

    @Query("""
                SELECT ai 
                FROM ArtInfo ai 
                JOIN ai.art a 
                WHERE a.id in (:artIds) 
                AND ai.expositionDateEnd IS NULL        
            """)
    List<ArtInfo> findLastByArtIds(@Param("artIds") List<UUID> artId);

    @Query("""
                SELECT ai 
                FROM ArtInfo ai 
                JOIN ai.art a 
                WHERE a.id = :artId
            """)
    List<ArtInfo> findByArtId(@Param("artId") UUID artId);
}
