package com.jabirinc.shopmebackend.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Created by Getinet on 9/18/21
 */
public class FileUploadUtil {

    public static final String FILE_SEPARATOR = "/";
    public static final String UPLOAD_FOLDER = "user-photos";
    public static final String UPLOAD_DIRECTORY = UPLOAD_FOLDER + FILE_SEPARATOR;

    public static final String DEFAULT_USER_IMAGE_PATH = "/images/default-user.png";


    public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile)
            throws IOException {

        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new IOException("Could not save file: " + fileName, ex);
        }
    }

    public static void cleanDirectory(String dir) {
        Path dirPath = Paths.get(dir);

        try {
            Files.list(dirPath).forEach(file -> {
                if (!Files.isDirectory(file)) {
                    try {
                        Files.delete(file);
                    } catch (IOException e) {
                        System.out.println("Could not delete file: " + file);
                    }
                }
            });
        } catch (IOException e) {
            System.out.println("Could not list directory: " + dirPath);
        }
    }
}
