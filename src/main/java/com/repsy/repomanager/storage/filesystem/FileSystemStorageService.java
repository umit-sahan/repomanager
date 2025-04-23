package com.repsy.repomanager.storage.filesystem;

import com.repsy.repomanager.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service("fileSystemStorageService")
public class FileSystemStorageService implements StorageService {

    @Value("${storage.filesystem.root}")
    private String rootDirectory;

    @Override
    public String saveFile(String filename, MultipartFile file) {
        try {
            Path storageDir = Paths.get(rootDirectory);
            if (!Files.exists(storageDir)) {
                Files.createDirectories(storageDir);
            }

            Path destination = storageDir.resolve(filename);
            Files.copy(file.getInputStream(), destination);

            log.info("File saved at: {}", destination.toAbsolutePath());
            return destination.toString();

        } catch (IOException e) {
            log.error("File saving error", e);
            throw new RuntimeException("Failed to store file " + filename, e);
        }
    }

    @Override
    public Resource loadFile(String filename) {
        try {
            Path file = Paths.get(rootDirectory).resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("File not found or unreadable: " + filename);
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not read file: " + filename, e);
        }
    }
}
