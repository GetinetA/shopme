package com.jabirinc.shopmebackend.utils;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Slf4j
public class FileUploadUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadUtil.class);

    public static final String FILE_SEPARATOR = "/";
    public static final String USER_UPLOAD_FOLDER = "user-photos";
    public static final String USER_UPLOAD_DIRECTORY = USER_UPLOAD_FOLDER + FILE_SEPARATOR;

    public static final String USER_DEFAULT_IMAGE_PATH = "/images/default-user.png";

    public static final String CATEGORY_UPLOAD_FOLDER = "category-images";
    public static final String CATEGORY_UPLOAD_DIRECTORY = CATEGORY_UPLOAD_FOLDER + FILE_SEPARATOR;
    public static final String CATEGORY_DEFAULT_IMAGE_PATH = "/images/image-thumbnail.png";

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
                        //System.out.println("Could not delete file: " + file);
                        log.error("Could not delete file: " + file); // using lombok
                        LOGGER.error("Could not delete file: " + file); // using static variable
                    }
                }
            });
        } catch (IOException e) {
            //System.out.println("Could not list directory: " + dirPath);
            log.error("Could not list directory: " + dirPath); // using lombok
            LOGGER.error("Could not list directory: " + dirPath); // using static variable
        }
    }

    public static void removeDirectory(String dir) {

        cleanDirectory(dir);

        try {
            Files.delete(Paths.get(dir));

        } catch (IOException e) {
            //System.out.println("Could not list directory: " + dirPath);
            log.error("Could not list directory: " + dir); // using lombok
            LOGGER.error("Could not list directory: " + dir); // using static variable
        }
    }
}
