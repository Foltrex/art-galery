package com.scnsoft.user.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.scnsoft.user.entity.Metadata;
import com.scnsoft.user.entity.MetadataId;

@Repository
public interface MetadataRepository extends JpaRepository<Metadata, MetadataId> {

    Optional<Metadata> findByMetadataIdAccountIdAndMetadataIdKey(UUID accountId, String key);

}
