package com.scnsoft.art.facade;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.scnsoft.art.dto.ArtFilter;
import com.scnsoft.art.dto.mapper.ArtInfoMapper;
import com.scnsoft.art.entity.ArtInfo;
import com.scnsoft.art.entity.EntityFile;
import com.scnsoft.art.service.ArtInfoService;
import com.scnsoft.art.service.CurrencyService;
import com.scnsoft.art.service.FileService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.scnsoft.art.dto.ArtDto;
import com.scnsoft.art.dto.mapper.ArtMapper;
import com.scnsoft.art.entity.Art;
import com.scnsoft.art.service.ArtService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ResponseStatusException;

import static com.scnsoft.art.app.AppInitialization.GEORGIAN_CURRENCY_VALUE;

@Component
@RequiredArgsConstructor
public class ArtServiceFacade {

    private final ArtService artService;
    private final FileService fileService;
    private final ArtMapper artMapper;
    private final ArtInfoService artInfoService;
    private final ArtInfoMapper artInfoMapper;
    private final CurrencyService currencyService;

    public ArtDto findById(UUID id) {
        ArtDto result = artMapper.mapToDto(artService.findById(id));
        result.setFiles(fileService.findAllByEntityId(id));
        return result;
    }

    public ArtDto save(ArtDto artDto) {
        Art art = artMapper.mapToEntity(artDto);

        art.setCurrency(currencyService.findAll().stream()
                .filter(c -> GEORGIAN_CURRENCY_VALUE.equals(c.getValue()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Currency not configured"
                )));
        var result = artMapper.mapToDto(artService.save(art, artDto.getFiles()));
        result.setFiles(fileService.findAllByEntityId(result.getId()));
        return result;
    }

    public void deleteById(UUID id) {
        artService.deleteById(id);
    }

    public void deleteByAccountId(UUID accountId) {
        artService.deleteByAccountId(accountId);
    }

    public Page<ArtDto> findAll(Pageable pageable, ArtFilter artFilter) {
        Page<Art> artPage = artService.findAll(pageable, artFilter);
        var ids = artPage
                .getContent()
                .stream()
                .map(Art::getId).toList();
        Map<UUID, EntityFile> primary = fileService
                .findAllThumbs(ids)
                .stream()
                .collect(Collectors.toMap(
                        EntityFile::getEntityId,
                        Function.identity(),
                        (v1, v2) -> v1
                ));

        Map<UUID, ArtInfo> lastInfo = artInfoService
                .findLastByArtIds(ids)
                .stream()
                .collect(Collectors.toMap(
                        (a) -> a.getArt().getId(),
                        Function.identity(),
                        (v1, v2) -> v1
                ));;
        var page = artMapper.mapPageToDto(artPage);
        page.getContent().forEach(art -> {
            var file = primary.get(art.getId());
            if(file != null) {
                art.setFiles(Collections.singletonList(file));
            }
            var info = lastInfo.get(art.getId());
            if(info != null) {
                art.setArtInfos(Collections.singletonList(artInfoMapper.mapToDto(info)));
            }
        });
        return page;
    }

}
