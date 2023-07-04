package com.scnsoft.art.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.scnsoft.art.dto.CityMergeDto;
import com.scnsoft.art.dto.MetadataEnum;
import com.scnsoft.art.repository.AccountRepository;
import com.scnsoft.art.repository.AddressRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.scnsoft.art.entity.City;
import com.scnsoft.art.repository.CityRepository;
import com.scnsoft.art.service.CityService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;
    private final AddressRepository addressRepository;
    private final AccountRepository accountRepository;

    @Override
    public Page<City> findAll(Pageable pageable) {
        return cityRepository.findAll(pageable);
    }

    @Override
    public List<City> findAll() {
        return cityRepository.findNotObsolete();
    }

    @Override
    public City findById(UUID id) {
        return cityRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "City with id " + id + " not found"));
    }

    @Override
    public City save(City city) {
        return save(city, false);
    }
    @Override
    public City save(City city, boolean force) {
        Optional<City> opt = force
                ? cityRepository.findByNameAndCountry(city.getName(), city.getCountry())
                : Optional.empty();

        return opt.map(existing -> {
                    if(existing.getSuccessor() == null) {
                        return existing;
                    }
                    return findById(existing.getSuccessor());
                })
                .orElseGet(() -> cityRepository.save(city));
    }

    @Override
    public City updateById(UUID id, City city) {
        city.setId(id);
        return cityRepository.save(city);
    }

    @Override
    public void deleteById(UUID id) {
        cityRepository.deleteById(id);
    }

    @Override
    public City merge(CityMergeDto cityDto) {
        City obsolete = findById(cityDto.getObsolete());
        City main = findById(cityDto.getMain());
        addressRepository.mergeCity(cityDto.getMain(), cityDto.getObsolete());
        accountRepository.mergeCity(MetadataEnum.CITY_ID, obsolete.getId(), main.getId());
        obsolete.setSuccessor(main.getId());
        save(obsolete);
        return main;
    }
}
