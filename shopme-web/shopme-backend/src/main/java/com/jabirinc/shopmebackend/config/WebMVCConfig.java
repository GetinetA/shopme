package com.jabirinc.shopmebackend.config;

import com.jabirinc.shopmebackend.utils.FileUploadUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Getinet on 9/18/21
 */
@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        Path userPhotosDir = Paths.get(FileUploadUtil.USER_UPLOAD_FOLDER);
        String userPhotosPath = userPhotosDir.toFile().getAbsolutePath();

        // java.net.UnknownHostException: Users in MacOS when using the following
        // .addResourceLocations("file:/" + userPhotosPath + "/");
        // syntax information https://en.wikipedia.org/wiki/File_URI_scheme
        registry.addResourceHandler("/" + FileUploadUtil.USER_UPLOAD_FOLDER + "/**")
                .addResourceLocations("file:" + userPhotosPath + "/");

    }
}
