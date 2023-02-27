package com.scnsoft.art.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.scnsoft.art.entity.Art;
import com.scnsoft.art.entity.Artist;

public interface ArtRepository extends JpaRepository<Art, UUID>, JpaSpecificationExecutor<Art> {

    
    // @Query("""
    //     SELECT a
    //     FROM Art a
    //     JOIN a.proposals p
    //     WHERE p.facility <> r.facility
    // """)
    // Page<Art> findAllByArtInfoIsNull(Pageable pageable);

    Page<Art> findAllByArtistAndName(Artist artist, Pageable pageable, String name);


    // Page<Art> findAllByArtInfoIsNull(Pageable pageable);


    Page<Art> findAllByArtInfoIsNull(Pageable pageable, String name);
    // Page<Art> findAllByArtInfoIsNull(Pageable pageable, String name);

    Page<Art> findAllByArtist(Artist artist, Pageable pageable);

    List<Art> findByArtist(Artist artist);
}
