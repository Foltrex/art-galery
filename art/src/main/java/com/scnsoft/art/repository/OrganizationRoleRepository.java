package com.scnsoft.art.repository;

import com.scnsoft.art.entity.OrganizationRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizationRoleRepository extends JpaRepository<OrganizationRole, Long> {

    Optional<OrganizationRole> findByName(OrganizationRole.RoleType name);

}
