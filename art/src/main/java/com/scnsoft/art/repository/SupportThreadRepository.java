package com.scnsoft.art.repository;

import com.scnsoft.art.entity.SupportThread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface SupportThreadRepository extends JpaRepository<SupportThread, UUID>, JpaSpecificationExecutor<SupportThread> {

}
