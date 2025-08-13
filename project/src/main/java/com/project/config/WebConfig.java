package com.project.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        String uploadPathUser = Paths.get("project/uploads/user").toAbsolutePath().toUri().toString();
        System.out.println("Upload Path for static resource: " + uploadPathUser);
        String uploadPathAdmin = Paths.get("project/uploads/admin").toAbsolutePath().toUri().toString();
        String uploadPathBook = Paths.get("project/uploads/book").toAbsolutePath().toUri().toString();
        registry.addResourceHandler("/uploads/user/**").addResourceLocations(uploadPathUser);
        registry.addResourceHandler("/uploads/admin/**").addResourceLocations(uploadPathAdmin);
        registry.addResourceHandler("/uploads/book/**").addResourceLocations(uploadPathBook);
    }
}
