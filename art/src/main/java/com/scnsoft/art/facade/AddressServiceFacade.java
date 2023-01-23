package com.scnsoft.art.facade;

import com.scnsoft.art.dto.AddressDto;
import com.scnsoft.art.dto.mapper.impl.AddressMapper;
import com.scnsoft.art.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

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
