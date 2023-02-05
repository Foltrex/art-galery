package com.scnsoft.art.service;

import com.scnsoft.art.entity.Representative;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface RepresentativeService {

    Page<Representative> findAll(Pageable pageable);

    Representative findById(UUID id);

    Representative findByAccountId(UUID accountId);

    Representative save(Representative representative);

    Representative update(UUID id, Representative representative);

    void deleteById(UUID id);

    void deleteByAccountId(UUID accountId);

}
