package com.scnsoft.art.repository;

import com.scnsoft.art.entity.Art;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ArtRepository extends JpaRepository<Art, UUID>, JpaSpecificationExecutor<Art> {

    @Query("SELECT a FROM Art a WHERE a.artInfos IS EMPTY AND a.name = :name")
    Page<Art> findAllByArtInfosIsEmptyAndName(Pageable pageable, @Param("name") String name);

    @Query("SELECT a FROM Art a WHERE a.artInfos IS EMPTY")
    Page<Art> findAllByArtInfosIsEmpty(Pageable pageable);
    
    List<Art> findByArtistAccountId(UUID artistAccountId);

//    Page<Art> findAllByArtistAccountId(UUID artistAccountId, Pageable pageable);
//
//    List<Art> findByArtistAccountId(UUID artistAccountId);
}
