package com.scnsoft.art.repository;

import com.scnsoft.art.entity.ArtStyle;
import com.scnsoft.art.entity.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtStyleRepository extends JpaRepository<ArtStyle, Integer> {

    @Query(value = "select art_style.id, art_style.label from art_style", nativeQuery = true)
    List<Option> findAllInOptionModel();
}
