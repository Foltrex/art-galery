package com.scnsoft.art.service.impl;

import com.scnsoft.art.entity.model.Option;
import com.scnsoft.art.repository.ArtSizeRepository;
import com.scnsoft.art.repository.ArtStyleRepository;
import com.scnsoft.art.service.OptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtStyleServiceImpl implements OptionService {

    private final ArtStyleRepository artStyleRepository;

    @Override
    public List<Option> findAll() {
        return artStyleRepository.findAllInOptionModel();
    }
}
