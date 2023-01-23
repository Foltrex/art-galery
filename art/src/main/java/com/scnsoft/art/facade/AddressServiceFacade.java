package com.scnsoft.art.facade;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.scnsoft.art.dto.AddressDto;
import com.scnsoft.art.dto.mapper.impl.AddressMapper;
import com.scnsoft.art.service.AddressService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AddressServiceFacade {
    private final AddressService addressService;
    private final AddressMapper addressMapper;

    public List<AddressDto> findAllByCityId(UUID cityId) {
        return addressService.findAllByCityId(cityId)
            .stream()
            .map(addressMapper::mapToDto)
            .toList();
    }
}
