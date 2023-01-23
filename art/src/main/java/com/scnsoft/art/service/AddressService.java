package com.scnsoft.art.service;

import com.scnsoft.art.entity.Address;
import com.scnsoft.art.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;

    public List<Address> findAllByCityId(UUID cityId) {
        return addressRepository.findAllByCityId(cityId);
    }
}
