package com.scnsoft.art.util;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.hibernate.service.spi.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.util.InvalidMimeTypeException;
import org.springframework.util.MimeType;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class ImageUtils {

    public static void main(String[] args) throws IOException {
        List<Path> images = new ArrayList<>(){{
            String root = "H:/art/art-galery/images/";
            add(Paths.get(root + "1.png"));
            add(Paths.get(root + "2.png"));
            add(Paths.get(root + "3.png"));
            add(Paths.get(root + "4.png"));
            add(Paths.get(root + "5.png"));
            add(Paths.get(root + "6.png"));
            add(Paths.get(root + "7.png"));
            add(Paths.get(root + "8.png"));
            add(Paths.get(root + "9.png"));
            add(Paths.get(root + "10.png"));
            add(Paths.get(root + "11.jpg"));
            add(Paths.get(root + "12.png"));
            add(Paths.get(root + "13.jpg"));
        }};
        for(Path p : images) {
            byte[] data = Files.readAllBytes(p);
            String name = p.getFileName().toString();

            String ext = name.substring(name.indexOf(".") + 1);
            resizeImage(data, MimeType.valueOf("application/" + ext).toString(), 500, 500)
                    .ifPresent(newBytes -> {
                        Path newPath = p.getParent().resolve(Path.of("thumb/" + name));
                        File file = new File(newPath.toString());
                        try {
                            Files.createDirectories(file.getParentFile().toPath());
                            if(Files.exists(file.toPath())) {
                                Files.delete(file.toPath());
                            }
                            Files.write(file.toPath(), newBytes, StandardOpenOption.CREATE_NEW);
                        } catch (IOException e) {
                            System.err.println("Failed to save file");
                        }
                    });
        }
    }


    public static Optional<byte[]> resizeImage(byte[] imageData, String mimeType, Integer cutWidth, Integer cutHeight) {
        Optional<String> typeOpt = getImageType(mimeType);
        if(typeOpt.isEmpty()) {
            return Optional.empty();
        }
        ByteArrayInputStream in = new ByteArrayInputStream(imageData);
        try {
            BufferedImage before = ImageIO.read(in);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int newWidth, newHeight;
            if (before.getWidth() > cutWidth || before.getHeight() > cutHeight) {
                int w = before.getWidth();
                int h = before.getHeight();
                double scaleX = before.getWidth() > cutWidth ? ((double)cutWidth) / w : 1;
                double scaleY = before.getHeight() > cutHeight ? ((double)cutHeight) / h : 1;
                double scale = Math.min(scaleY, scaleX);

                if(scaleY < scaleX) {
                    newHeight = cutHeight;
                    newWidth = (int)(w * scale);
                } else {
                    newHeight = (int)(h * scale);
                    newWidth = cutWidth;
                }
            } else {
                newWidth = before.getWidth();
                newHeight = before.getHeight();
            }
            Thumbnails.of(before)
                    .size(newWidth, newHeight)
                    .outputFormat(typeOpt.get())
                    .toOutputStream(buffer);
            imageData = buffer.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Failed to process image");
        }
        return Optional.of(imageData);
    }

    private static Optional<String> getImageType(String mime) {
        if(mime.isEmpty()) {
            return Optional.empty();
        }
        try {
            String type = MimeType.valueOf(mime).getSubtype().toLowerCase();
            switch (type) {
                case "jpeg":
                case "jpg":
                    return Optional.of("jpg");
                case "png":
                case "bmp":
                case "gif":
                    return Optional.of(type);
                default:
                    log.error("Failed to parse mime type from " + mime);
                    return Optional.of(type);
            }
        } catch (InvalidMimeTypeException e) {
            log.error("Failed to parse mime type from " + mime, e);
            return Optional.empty();
        }
    }
}
