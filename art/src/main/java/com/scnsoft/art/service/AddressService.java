package com.scnsoft.art.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.scnsoft.art.entity.Address;
import com.scnsoft.art.repository.AddressRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;

    public List<Address> findAllByCityId(UUID cityId) {
        return addressRepository.findAllByCityId(cityId);
    }
}
