package com.scnsoft.art.repository;

import com.scnsoft.art.entity.ArtType;
import com.scnsoft.art.entity.model.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtTypeRepository extends JpaRepository<ArtType, Integer> {

    @Query(value = "select art_type.id, art_type.label from art_type", nativeQuery = true)
    List<Option> findAllInOptionModel();
}
