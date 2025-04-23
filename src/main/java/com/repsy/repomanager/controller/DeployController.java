package com.repsy.repomanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.repsy.repomanager.model.PackageMetadata;
import com.repsy.repomanager.repository.PackageMetadataRepository;
import com.repsy.repomanager.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class DeployController {

    private final PackageMetadataRepository metadataRepository;
    private final StorageService storageService;
    private final ObjectMapper objectMapper;

    @PostMapping("{packageName}/{version}")
    public ResponseEntity<?> uploadPackage(
            @PathVariable String packageName,
            @PathVariable String version,
            @RequestParam("package") MultipartFile packageFile,
            @RequestParam("meta") MultipartFile metaFile
    ) {


        try {
            Map<String, Object> metaMap = objectMapper.readValue(metaFile.getInputStream(), Map.class);


            PackageMetadata metadata = PackageMetadata.builder()
                    .name(packageName)
                    .version(version)
                    .author((String) metaMap.get("author"))
                    .dependenciesJson(metaFile.getInputStream().readAllBytes().toString())
                    .build();


            String savedPath = storageService.saveFile(packageName + "-" + version + ".rep", packageFile);


            metadata.setFilePath(savedPath);
            metadataRepository.save(metadata);

            return ResponseEntity.ok("Package uploaded successfully.");

        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Invalid meta.json content: " + e.getMessage());
        }
    }
}
