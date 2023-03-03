package com.scnsoft.art.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.scnsoft.art.entity.Art;
import com.scnsoft.art.entity.Artist;

public interface ArtRepository extends JpaRepository<Art, UUID>, JpaSpecificationExecutor<Art> {

    Page<Art> findAllByArtistAndName(Artist artist, Pageable pageable, String name);

    @Query("SELECT a FROM Art a WHERE a.artInfos IS EMPTY AND a.name = :name")
    Page<Art> findAllByArtInfosIsEmptyAndName(Pageable pageable, @Param("name") String name);

    @Query("SELECT a FROM Art a WHERE a.artInfos IS EMPTY")
    Page<Art> findAllByArtInfosIsEmpty(Pageable pageable);

    Page<Art> findAllByArtist(Artist artist, Pageable pageable);

    List<Art> findByArtist(Artist artist);
}
