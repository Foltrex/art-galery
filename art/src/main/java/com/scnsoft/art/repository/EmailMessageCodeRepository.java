package com.scnsoft.art.repository;

import com.scnsoft.art.entity.EmailMessageCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Modifying
    @Query("update EmailMessageCode message set message.isValid = false where message.id=:emailMessageCodeId")
    void updateSetCodeIsInvalidById(@Param("emailMessageCodeId") UUID emailMessageCodeId);

}