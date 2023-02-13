package com.scnsoft.user.repository;

import com.scnsoft.user.entity.EmailMessageCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmailMessageCodeRepository extends JpaRepository<EmailMessageCode, UUID> {

}
