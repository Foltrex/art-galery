package com.scnsoft.user.repository;

import com.scnsoft.user.entity.Metadata;
import com.scnsoft.user.entity.MetadataId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MetadataRepository extends JpaRepository<Metadata, MetadataId> {

    @Query(
            value = "delete from account_metadata " +
                    "where account_metadata.account_id = ?1 " +
                    "and account_metadata.key = ?2",
            nativeQuery = true)
    void deleteByAccountIdAndKey(@Param("accountId") UUID accountId, @Param("key") String key);

    @Modifying
    @Query(value = "update Metadata metadata set metadata.value=:value where metadata.metadataId=:id")
    void updateValueById(@Param("id") MetadataId metadataId, @Param("value") String value);
}
