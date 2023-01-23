package com.scnsoft.art.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.scnsoft.art.entity.Address;
import com.scnsoft.art.entity.City;

public interface AddressRepository extends JpaRepository<Address, UUID> {
    @Query("SELECT a FROM Address a WHERE a.city.id = :cityId")
    List<Address> findAllByCityId(@Param("cityId") UUID cityId);
}
