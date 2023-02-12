package com.scnsoft.file.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Service
@RequiredArgsConstructor
public class DocumentService {

    public InputStream getInputStream(String filePath) {
        try {
            return Files.newInputStream(Paths.get(filePath));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Troubles with file reading", e);
        }
    }

    public void upload(String filePath, byte[] fileData) {
        File file = new File(filePath);
        try {
            Files.createDirectories(file.getParentFile().toPath());
            Files.write(file.toPath(), fileData, StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot save file to path: " + filePath);
        }
    }

    public void remove(String filePath) {
        File file = new File(filePath);
        try {
            Files.delete(file.toPath());
        } catch (NoSuchFileException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    (String.format("Cannot found file with path : %s", file)));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    (String.format("Troubles with deleting file with path = %s", filePath)));
        }
    }

}
