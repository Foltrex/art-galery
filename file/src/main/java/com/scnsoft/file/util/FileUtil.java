package com.scnsoft.file.util;

import org.springframework.util.MimeType;

import java.io.File;
import java.nio.file.FileSystems;
import java.util.Random;
import java.util.UUID;

public class FileUtil {

    private final static String SEPARATOR = FileSystems.getDefault().getSeparator();

    public static String generateSystemFilename() {
        Random random = new Random();
        StringBuilder filename = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            char ch = (char) (random.nextInt(26) + 'a');
            filename.append(Character.valueOf(ch));
        }

        return filename.toString();
    }

    public static String getSystemFilenameFromPath(String filePath) {
        int indexPoint = filePath.indexOf('.');
        String pathWithoutFileExtension = filePath.substring(0, indexPoint);
        String[] array = pathWithoutFileExtension.split("/");

        return array[array.length - 1];
    }

    public static String retrieveFolderPath(UUID artId, String defaultPath) {
        String id = artId.toString();
        StringBuilder absolutePath = new StringBuilder();

        absolutePath.append(defaultPath);

        char lastChar = defaultPath.charAt(defaultPath.length() - 1);
        char separator = SEPARATOR.toCharArray()[0];
        if (lastChar != separator) {
            absolutePath.append(SEPARATOR);
        }

        absolutePath.append(id, 0, 3);
        absolutePath.append(SEPARATOR);
        absolutePath.append(id, 3, 6);
        absolutePath.append(SEPARATOR);
        absolutePath.append(id, 6, 8);
        absolutePath.append(id, 9, 10);
        absolutePath.append(SEPARATOR);
        absolutePath.append(id);

        return absolutePath.toString();
    }

    public static String buildFilePath(UUID artId, String systemFilename, String mimeType, String defaultPath) {
        String folderPath = FileUtil.retrieveFolderPath(artId, defaultPath);
        String fileExtension = MimeType.valueOf(mimeType).getSubtype();

        return String.format("%s/%s.%s", folderPath, systemFilename, fileExtension);
    }

    public static String generateNewFilePath(UUID artId, String mimeType, String defaultPath) {
        String filePath = buildFilePath(artId, FileUtil.generateSystemFilename(), mimeType, defaultPath);

        File file = new File(filePath);
        while (file.exists()) {
            filePath = buildFilePath(artId, FileUtil.generateSystemFilename(), mimeType, defaultPath);
            file = new File(String.valueOf(filePath));
        }

        return filePath;
    }

}
