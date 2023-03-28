package com.scnsoft.art.service.impl;

import com.scnsoft.art.entity.model.Option;
import com.scnsoft.art.repository.ArtTopicRepository;
import com.scnsoft.art.service.OptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtTopicServiceImpl implements OptionService {

    private final ArtTopicRepository artTopicRepository;

    @Override
    public List<Option> findAll() {
        return artTopicRepository.findAllInOptionModel();
    }

}
