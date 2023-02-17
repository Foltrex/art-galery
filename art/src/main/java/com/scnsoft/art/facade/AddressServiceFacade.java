package com.scnsoft.art.facade;

import com.scnsoft.art.dto.AddressDto;
import com.scnsoft.art.dto.mapper.AddressMapper;
import com.scnsoft.art.service.impl.AddressServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AddressServiceFacade {
    private final AddressServiceImpl addressService;
    private final AddressMapper addressMapper;

    public Page<AddressDto> findAllByCityId(UUID cityId, Pageable pageable) {
        return addressMapper.mapPageToDto(addressService.findAllByCityId(cityId, pageable));
    }
}
