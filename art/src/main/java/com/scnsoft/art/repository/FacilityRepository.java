package com.scnsoft.art.repository;

import com.scnsoft.art.entity.Facility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FacilityRepository extends JpaRepository<Facility, UUID> {

}
