package com.scnsoft.art.service.impl;

import com.scnsoft.art.entity.model.Option;
import com.scnsoft.art.repository.ArtTypeRepository;
import com.scnsoft.art.service.OptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtTypeServiceImpl implements OptionService {

    private final ArtTypeRepository artTypeRepository;

    @Override
    public List<Option> findAll() {
        return artTypeRepository.findAllInOptionModel();
    }

}
