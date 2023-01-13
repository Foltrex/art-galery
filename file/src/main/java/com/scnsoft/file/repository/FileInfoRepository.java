package com.scnsoft.file.repository;

import com.scnsoft.file.entity.FileInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FileInfoRepository extends JpaRepository<FileInfo, UUID> {

    Page<FileInfo> findAllByArtId(UUID artId, Pageable pageable);

}
