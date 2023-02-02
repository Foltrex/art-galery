package com.scnsoft.art.repository;

import com.scnsoft.art.entity.Facility;
import com.scnsoft.art.entity.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface FacilityRepository extends JpaRepository<Facility, UUID> {

    Page<Facility> findAllByOrganization(Organization organization, Pageable pageable);

    List<Facility> findAllByOrganization(Organization organization);

    @Query("SELECT facility FROM Facility facility WHERE facility.organization.id = :organizationId")
    Page<Facility> findAllByOrganizationId(@Param("organizationId") UUID organizationId, Pageable pageable);

}
