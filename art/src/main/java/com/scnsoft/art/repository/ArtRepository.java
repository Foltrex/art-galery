package com.scnsoft.art.repository;

import com.scnsoft.art.entity.Art;
import com.scnsoft.art.entity.Artist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import java.util.List;

public interface ArtRepository extends JpaRepository<Art, UUID> {

    Page<Art> findAllByArtist(Artist artist, Pageable pageable);

    List<Art> findByArtist(Artist artist);
}
