package com.scnsoft.art.repository;

import com.scnsoft.art.entity.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FileInfoRepository extends JpaRepository<FileInfo, UUID>, JpaSpecificationExecutor<FileInfo> {
    List<FileInfo> findByDirectoryStartingWith(String dir);

//    List<FileInfo> findAllByArtId(UUID artId);

}
