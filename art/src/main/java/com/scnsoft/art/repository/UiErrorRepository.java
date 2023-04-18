package com.scnsoft.art.repository;

import com.scnsoft.art.entity.UiError;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UiErrorRepository extends JpaSpecificationExecutor<UiError>, JpaRepository<UiError, Integer> {
}
