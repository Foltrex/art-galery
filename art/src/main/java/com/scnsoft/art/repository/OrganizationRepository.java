package com.scnsoft.art.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.scnsoft.art.entity.Organization;

@Repository
public interface OrganizationRepository
		extends JpaRepository<Organization, UUID>, JpaSpecificationExecutor<Organization> {
	
	Organization findByName(String name);
}
