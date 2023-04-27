package com.scnsoft.file.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentService {

    public InputStream getInputStream(String filePath) {
        try {
            return Files.newInputStream(Paths.get(filePath));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File not found", e);
        }
    }

    public void upload(String filePath, byte[] fileData) {
        File file = new File(filePath);
        try {
            Files.createDirectories(file.getParentFile().toPath());
            Files.write(file.toPath(), fileData, StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot save file: " + filePath);
        }
    }

    public void remove(String filePath) {
        File file = new File(filePath);
        try {
            Files.delete(file.toPath());
        } catch (NoSuchFileException e) {
            log.info("attempt to delete file which does not exists " + filePath);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    (String.format("Cannot delete file by path = %s", filePath)));
        }
    }

}
