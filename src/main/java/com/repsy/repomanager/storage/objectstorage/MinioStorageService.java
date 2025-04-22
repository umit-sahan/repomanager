package com.repsy.repomanager.storage.objectstorage;

import com.repsy.repomanager.service.StorageService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.GetObjectArgs;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Slf4j
@Service("minioStorageService")
public class MinioStorageService implements StorageService {

    @Value("${storage.object.endpoint}")
    private String endpoint;

    @Value("${storage.object.access-key}")
    private String accessKey;

    @Value("${storage.object.secret-key}")
    private String secretKey;

    @Value("${storage.object.bucket}")
    private String bucket;

    private MinioClient minioClient;

    @PostConstruct
    public void init() {
        minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }

    @Override
    public String saveFile(String filename, MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(filename)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            log.info("File uploaded to MinIO: {}/{}", bucket, filename);
            return filename;

        } catch (Exception e) {
            log.error("MinIO upload failed", e);
            throw new RuntimeException("Failed to upload to MinIO", e);
        }
    }

    @Override
    public Resource loadFile(String filename) {
        try (InputStream inputStream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucket)
                        .object(filename)
                        .build())) {

            byte[] content = inputStream.readAllBytes();
            return new ByteArrayResource(content);

        } catch (Exception e) {
            log.error("MinIO download failed", e);
            throw new RuntimeException("Failed to download file from MinIO: " + filename, e);
        }
    }
}
