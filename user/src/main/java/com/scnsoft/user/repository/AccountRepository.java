package com.scnsoft.user.repository;

import com.scnsoft.user.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    Optional<Account> findByEmail(String email);

    @Query(value = "select account.* from account " +
            "join account_metadata am on account.id = am.account_id " +
            "where account_type = 'REPRESENTATIVE' " +
            "and am.key = 'organizationId' " +
            "and am.value = ?1", nativeQuery = true)
    Page<Account> findAllByOrganizationId(String organizationId, Pageable pageable);
}
