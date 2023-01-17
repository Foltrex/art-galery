package com.scnsoft.art.repository;

import com.scnsoft.art.entity.Facility;
import com.scnsoft.art.entity.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FacilityRepository extends JpaRepository<Facility, UUID> {

    Page<Facility> findAllByOrganization(Organization organization, Pageable pageable);

}
