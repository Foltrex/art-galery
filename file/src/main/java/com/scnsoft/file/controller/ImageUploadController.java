package com.scnsoft.file.controller;

import com.scnsoft.file.dto.ImageDto;
import com.scnsoft.file.service.DocumentService;
import com.scnsoft.file.service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("images")
public class ImageUploadController {

    private final ImageUploadService imageUploadService;
    private final DocumentService documentService;

    @PostMapping
    public void uploadImage(@RequestBody ImageDto imageDto) {
        System.out.println(imageDto.toString());
        imageUploadService.uploadAvatar(imageDto);
    }

    @DeleteMapping("/{id}")
    public void deleteImage(@PathVariable("id") Long id) {
        documentService.deleteAvatarByUserId(id);
    }
}
