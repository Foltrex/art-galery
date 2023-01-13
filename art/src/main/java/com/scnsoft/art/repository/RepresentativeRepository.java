package com.scnsoft.art.repository;

import com.scnsoft.art.entity.Organization;
import com.scnsoft.art.entity.Representative;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RepresentativeRepository extends JpaRepository<Representative, UUID> {

    Optional<Representative> findByAccountId(UUID accountId);

    Page<Representative> findAllByOrganization(Organization organization, Pageable pageable);
}
