package com.scnsoft.file.repository;

import com.scnsoft.file.entity.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FileInfoRepository extends JpaRepository<FileInfo, UUID> {

//    List<FileInfo> findAllByArtId(UUID artId);

}
