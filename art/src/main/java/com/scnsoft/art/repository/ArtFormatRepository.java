package com.scnsoft.art.repository;

import com.scnsoft.art.entity.ArtFormat;
import com.scnsoft.art.entity.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtFormatRepository extends JpaRepository<ArtFormat, Integer> {

    @Query("select artFormat.id, artFormat.label from ArtFormat artFormat")
    List<Option> findAllInOptionModel();
}
