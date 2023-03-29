package com.scnsoft.art.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.scnsoft.user.entity.Address;

import java.util.UUID;

public interface AddressService {

    Page<Address> findAllByCityId(UUID cityId, Pageable pageable);

}
