package com.scnsoft.art.repository;

import java.util.Optional;
import java.util.UUID;

import com.scnsoft.art.dto.MetadataEnum;
import com.scnsoft.art.entity.Account;
import liquibase.pro.packaged.Q;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;



@Repository
public interface AccountRepository extends JpaRepository<Account, UUID>, JpaSpecificationExecutor<Account> {

    Optional<Account> findByEmail(String email);

    @Modifying
    @Query("update Metadata m set m.value = :main where m.metadataId.key = :cityId and m.value = :obsolete")
    void mergeCity(MetadataEnum cityId, UUID obsolete, UUID main);
}
