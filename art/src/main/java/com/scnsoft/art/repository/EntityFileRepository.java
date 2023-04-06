package com.scnsoft.art.repository;

import com.scnsoft.art.entity.EntityFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EntityFileRepository extends JpaRepository<EntityFile, UUID> {

    List<EntityFile> findAllByEntityId(UUID entityId);
}
