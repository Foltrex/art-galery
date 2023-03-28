package com.scnsoft.art.repository;

import com.scnsoft.art.entity.ArtFormat;
import com.scnsoft.art.entity.ArtStyle;
import com.scnsoft.art.entity.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtStyleRepository extends JpaRepository<ArtStyle, Integer> {

    @Query("select artStyle.id, artStyle.label from ArtStyle artStyle")
    List<Option> findAllInOptionModel();
}
