package com.scnsoft.art.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.scnsoft.art.entity.Address;

import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
    @Query("SELECT a FROM Address a JOIN a.city c WHERE c.id = :cityId")
    Page<Address> findAllByCityId(@Param("cityId") UUID cityId, Pageable pageable);

    @Modifying
    @Query(nativeQuery = true, value = "update address set city_id = :main where city_id = :obsolete")
    void mergeCity(@Param("main") UUID main, @Param("obsolete") UUID obsolete);
}
