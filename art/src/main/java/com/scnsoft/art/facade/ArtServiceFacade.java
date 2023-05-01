package com.scnsoft.art.facade;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.scnsoft.art.dto.ArtFilter;
import com.scnsoft.art.dto.UploadEntityFileDto;
import com.scnsoft.art.dto.mapper.ArtInfoMapper;
import com.scnsoft.art.entity.ArtInfo;
import com.scnsoft.art.entity.EntityFile;
import com.scnsoft.art.service.ArtInfoService;
import com.scnsoft.art.service.FileService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.scnsoft.art.dto.ArtDto;
import com.scnsoft.art.dto.mapper.ArtMapper;
import com.scnsoft.art.entity.Art;
import com.scnsoft.art.service.ArtService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ArtServiceFacade {

    private final ArtService artService;
    private final FileService fileService;
    private final ArtMapper artMapper;
    private final ArtInfoService artInfoService;
    private final ArtInfoMapper artInfoMapper;

    public ArtDto findById(UUID id) {
        return artMapper.mapToDto(artService.findById(id));
    }

    public ArtDto save(ArtDto artDto) {
        Art art = artMapper.mapToEntity(artDto);
        return artMapper.mapToDto(artService.save(art));
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


    public EntityFile uploadImage(UUID id, UploadEntityFileDto uploadEntityFileDto) {
        return artService.uploadImage(id, uploadEntityFileDto);
    }
}
