package com.scnsoft.art.service;

import com.scnsoft.art.dto.FacilityFilter;
import com.scnsoft.art.entity.EntityFile;
import com.scnsoft.art.entity.Facility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FacilityService {

    Optional<Facility> findById(UUID id);

    Page<Facility> findAll(Pageable pageable);

    Page<Facility> findAll(Pageable pageable, FacilityFilter filter);

    List<Facility> findAll(FacilityFilter filter);
    Facility save(Facility facility, List<EntityFile> images);

    List<Facility> findAll();

    void deleteById(UUID id);

}
