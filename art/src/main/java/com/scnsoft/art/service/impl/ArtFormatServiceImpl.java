package com.scnsoft.art.service.impl;

import com.scnsoft.art.entity.model.Option;
import com.scnsoft.art.repository.ArtFormatRepository;
import com.scnsoft.art.repository.ArtSizeRepository;
import com.scnsoft.art.service.OptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtFormatServiceImpl implements OptionService {

    private final ArtFormatRepository artFormatRepository;

    @Override
    public List<Option> findAll() {
        return artFormatRepository.findAllInOptionModel();
    }

}
