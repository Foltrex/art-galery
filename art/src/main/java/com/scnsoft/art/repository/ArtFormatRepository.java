package com.scnsoft.art.repository;

import com.scnsoft.art.entity.ArtFormat;
import com.scnsoft.art.entity.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtFormatRepository extends JpaRepository<ArtFormat, Integer> {

    @Query(value = "select art_format.id, art_format.label from art_format", nativeQuery = true)
    List<Option> findAllInOptionModel();
}
