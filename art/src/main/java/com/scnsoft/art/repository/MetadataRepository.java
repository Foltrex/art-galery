package com.scnsoft.art.repository;

import java.util.Optional;
import java.util.UUID;

import com.scnsoft.art.entity.Metadata;
import com.scnsoft.art.entity.MetadataId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MetadataRepository extends JpaRepository<Metadata, MetadataId> {

    Optional<Metadata> findByMetadataIdAccountIdAndMetadataIdKey(UUID accountId, String key);

}
