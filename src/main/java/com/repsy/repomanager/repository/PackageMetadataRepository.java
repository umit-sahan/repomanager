package com.repsy.repomanager.repository;

import com.repsy.repomanager.model.PackageMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PackageMetadataRepository extends JpaRepository<PackageMetadata, UUID> {
    Optional<PackageMetadata> findByNameAndVersion(String name, String version);
}
