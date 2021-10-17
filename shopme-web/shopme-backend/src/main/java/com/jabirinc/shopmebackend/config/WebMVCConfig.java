package com.jabirinc.shopmebackend.config;

import com.jabirinc.shopmebackend.utils.FileUploadUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

/**
 * Created by Getinet on 9/18/21
 */
@Configuration
@EnableSwagger2
public class WebMVCConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        /** Users photo resource **/
        exposeDirectory(registry, FileUploadUtil.USER_UPLOAD_FOLDER);


        /** Categories image resource **/
        exposeDirectory(registry, FileUploadUtil.CATEGORY_UPLOAD_FOLDER);

        /** Brands image resource **/
        exposeDirectory(registry, FileUploadUtil.BRAND_UPLOAD_FOLDER);
    }

    /**
     * Expose directory to the clients
     * java.net.UnknownHostException: Users in MacOS when using the following
     *         // .addResourceLocations("file:/" + userPhotosPath + "/");
     *         // syntax information https://en.wikipedia.org/wiki/File_URI_scheme
     *
     * @param registry
     * @param pathPattern
     */
    private void exposeDirectory(ResourceHandlerRegistry registry, String pathPattern) {
        Path pathDir = Paths.get(pathPattern);
        String absolutePath = pathDir.toFile().getAbsolutePath();

        String logicalPath = "/" + pathPattern + "/**";

        registry.addResourceHandler(logicalPath)
                .addResourceLocations("file:" + absolutePath + "/");
    }

    @Bean
    public Docket swaggerConfiguration() {

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.ant("/api/*"))
                .apis(RequestHandlerSelectors.basePackage("com.jabirinc.shopmebackend"))
                .build()
                // adding application metadata
                .apiInfo(apiDetails());
    }

    private ApiInfo apiDetails() {
        return new ApiInfo(
                "Shopme Admin API",
                "API for Shopme Admin Project",
                "1.0",
                "Free to use",
                new springfox.documentation.service.Contact("Getinet", "http://www.localhost:8080/shopmeAdmin", ""),
                "API License",
                "http://www.localhost:8080/shopmeAdmin",
                Collections.emptyList());
    }
}
