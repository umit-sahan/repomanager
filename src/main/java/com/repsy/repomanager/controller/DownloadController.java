package com.repsy.repomanager.controller;

import com.repsy.repomanager.model.PackageMetadata;
import com.repsy.repomanager.repository.PackageMetadataRepository;
import com.repsy.repomanager.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class DownloadController {

    private final PackageMetadataRepository metadataRepository;
    private final StorageService storageService;

    @GetMapping("{packageName}/{version}/{fileName}")
    public ResponseEntity<?> downloadPackage(
            @PathVariable String packageName,
            @PathVariable String version,
            @PathVariable String fileName
    ) {
        Optional<PackageMetadata> metaOpt = metadataRepository.findByNameAndVersion(packageName, version);
        if (metaOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            // Dosyayı oku (dosya sisteminden veya minio'dan)
            Resource resource = storageService.loadFile(fileName);

            String contentType = Files.probeContentType(Paths.get(fileName));
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType != null ? contentType : "application/octet-stream"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("File cannot be downloaded: " + e.getMessage());
        }
    }
}
