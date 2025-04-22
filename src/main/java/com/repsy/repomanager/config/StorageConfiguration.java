package com.repsy.repomanager.config;

import com.repsy.repomanager.service.StorageService;
import com.repsy.repomanager.storage.filesystem.FileSystemStorageService;
import com.repsy.repomanager.storage.objectstorage.MinioStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfiguration {

    @Value("${storage.strategy}")
    private String strategy;

    private final FileSystemStorageService fileSystem;
    private final MinioStorageService minio;

    public StorageConfiguration(FileSystemStorageService fileSystem, MinioStorageService minio) {
        this.fileSystem = fileSystem;
        this.minio = minio;
    }

    @Bean
    public StorageService storageService() {
        return switch (strategy) {
            case "file-system" -> fileSystem;
            case "object-storage" -> minio;
            default -> throw new IllegalArgumentException("Unknown storage strategy: " + strategy);
        };
    }
}
