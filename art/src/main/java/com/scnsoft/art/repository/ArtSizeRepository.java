package com.scnsoft.art.repository;

import com.scnsoft.art.entity.ArtSize;
import com.scnsoft.art.entity.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtSizeRepository extends JpaRepository<ArtSize, Integer> {

    @Query(value = "select art_size.id, art_size.label from art_size", nativeQuery = true)
    List<Option> findAllInOptionModel();
}
