package com.scnsoft.art.repository;

import com.scnsoft.art.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, UUID> {

    Optional<Artist> findByAccountId(UUID accountId);

    void deleteArtistByAccountId(UUID accountId);
}
