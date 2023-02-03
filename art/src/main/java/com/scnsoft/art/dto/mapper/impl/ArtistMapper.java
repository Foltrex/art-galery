package com.scnsoft.art.dto.mapper.impl;

import com.scnsoft.art.dto.AddressDto;
import com.scnsoft.art.dto.ArtistDto;
import com.scnsoft.art.dto.mapper.Mapper;
import com.scnsoft.art.entity.Address;
import com.scnsoft.art.entity.Artist;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ArtistMapper implements Mapper<Artist, ArtistDto> {

    private final AddressMapper addressMapper;

    @Override
    public ArtistDto mapToDto(Artist artist) {
        return ArtistDto.builder()
                .id(artist.getId())
                .address(mapAddressToDto(artist.getAddress()))
                .firstname(artist.getFirstname())
                .lastname(artist.getLastname())
                .description(artist.getDescription())
                .accountId(artist.getAccountId())
                .build();
    }

    @Override
    public Artist mapToEntity(ArtistDto artistDto) {
        return artistDto != null
                ? Artist.builder()
                .id(artistDto.getId())
                .address(mapAddressDtoToEntity(artistDto.getAddress()))
                .firstname(artistDto.getFirstname())
                .lastname(artistDto.getLastname())
                .description(artistDto.getDescription())
                .accountId(artistDto.getAccountId())
                .build()
                : null;
    }

    public Page<ArtistDto> mapPageToDto(final Page<Artist> artistPage) {
        return new PageImpl<>(artistPage.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList()), artistPage.getPageable(), artistPage.getTotalElements());
    }

    private AddressDto mapAddressToDto(Address address) {
        return address != null ? addressMapper.mapToDto(address) : null;
    }

    private Address mapAddressDtoToEntity(AddressDto addressDto) {
        return addressDto != null ? addressMapper.mapToEntity(addressDto) : null;
    }

}
