package com.scnsoft.user.repository;

import com.scnsoft.user.entity.EmailMessageCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmailMessageCodeRepository extends JpaRepository<EmailMessageCode, UUID> {

    @Query(nativeQuery = true,
            value = "select * from email_message_code " +
                    "where email_message_code.account_id=:accountId " +
                    "order by email_message_code.created_at " +
                    "desc " +
                    "limit 1")
    Optional<EmailMessageCode> findLastByAccountId(@Param("accountId") UUID accountId);

}
