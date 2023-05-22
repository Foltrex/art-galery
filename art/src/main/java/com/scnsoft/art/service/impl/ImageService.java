package com.scnsoft.art.service.impl;

import com.scnsoft.art.dto.FileInfoDto;
import com.scnsoft.art.entity.EntityFile;
import com.scnsoft.art.entity.FileInfo;
import com.scnsoft.art.service.FileService;
import com.scnsoft.art.service.FileServiceImplFile;
import com.scnsoft.art.util.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final FileService fileService;
    private final FileServiceImplFile fileServiceExternal;

    public void processImages(
            UUID entityId, List<EntityFile> images, String newDirName,
            Integer width, Integer height, Integer optThumbWidth, Integer optThumbHeight) {
        List<EntityFile> existingImages = fileService.findAllByEntityId(entityId);

        Map<UUID, EntityFile> imageMap = images.stream().collect(Collectors.toMap(
                EntityFile::getId,
                e -> e
        ));
        Map<UUID, EntityFile> existingMap = new HashMap<>();
        for(EntityFile e : existingImages) {
            if(!imageMap.containsKey(e.getId())) {
                fileService.delete(e.getId());
            } else {
                existingMap.put(e.getId(), e);
            }
        }
        for(EntityFile image : images) {
            if(existingMap.containsKey(image.getId())) {
                fileService.save(image);
                continue;
            }
            fileService.findById(image.getId()).ifPresent(e -> {
                if(!e.getEntityId().equals(entityId)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Image already used by another object");
                }
            });

            image.setEntityId(entityId);

            fileServiceExternal.findFileInfoById(image.getId()).ifPresent(info -> {
                info.setDirectory(newDirName);
                info.setCacheControl(-1);
                FileServiceImplFile.FileWrapper stream = fileServiceExternal.getFileStream(info.getId());
                try(var s = stream.inputStream()) {
                    byte[] initial = s.readAllBytes();
                    byte[] bytes = ImageUtils.resizeImage(initial, info.getMimeType(), width, height)
                                    .orElseThrow(() -> new ResponseStatusException(
                                            HttpStatus.INTERNAL_SERVER_ERROR,
                                            "Failed to resize image with id: " + info.getId()));
                    fileServiceExternal.upstream(info, bytes);
                    image.setType(EntityFile.Type.ORIGINAL);
                    EntityFile savedImage = fileService.save(image);

                    if(optThumbHeight != null && optThumbWidth != null) {

                        byte[] thumbBytes = ImageUtils.resizeImage(initial, info.getMimeType(), width, height)
                                .orElseThrow(() -> new ResponseStatusException(
                                        HttpStatus.INTERNAL_SERVER_ERROR,
                                        "Failed to resize image with id: " + info.getId()));

                        FileInfoDto dto = new FileInfoDto();
                        dto.setMimeType(info.getMimeType());
                        dto.setDirectory(newDirName);
                        dto.setCacheControl(-1);
                        dto.setData(Base64.getEncoder().encodeToString(thumbBytes));
                        FileInfo thumb = fileServiceExternal.uploadFile(dto);
                        EntityFile thumbEntity = new EntityFile();
                        thumbEntity.setId(thumb.getId());
                        thumbEntity.setEntityId(entityId);
                        thumbEntity.setOriginalId(savedImage.getId());
                        thumbEntity.setIsPrimary(savedImage.getIsPrimary());
                        thumbEntity.setType(EntityFile.Type.THUMBNAIL);
                        thumbEntity.setCreationDate(new Date());
                        fileService.save(thumbEntity);
                    }

                } catch (IOException e) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to upload file, internal server error (IO)");
                }
            });

        }
    }
}
