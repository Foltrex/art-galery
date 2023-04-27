package com.scnsoft.art.repository;

import java.util.UUID;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.scnsoft.art.entity.Organization;

@Repository
public interface OrganizationRepository
		extends JpaRepository<Organization, UUID>, JpaSpecificationExecutor<Organization> {
	
	Organization findByName(String name);
}
