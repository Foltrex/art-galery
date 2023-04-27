package com.scnsoft.art.service.impl;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.scnsoft.art.repository.AddressRepository;
import com.scnsoft.art.service.AddressService;
import com.scnsoft.art.entity.Address;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;

    @Override
    public Page<Address> findAllByCityId(UUID cityId, Pageable pageable) {
        return addressRepository.findAllByCityId(cityId, pageable);
    }
}
