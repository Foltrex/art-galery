package com.scnsoft.user.repository;

import com.scnsoft.user.entity.Metadata;
import com.scnsoft.user.entity.MetadataId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetadataRepository extends JpaRepository<Metadata, MetadataId> {
}
