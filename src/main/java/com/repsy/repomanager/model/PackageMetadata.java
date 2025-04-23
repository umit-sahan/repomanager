package com.repsy.repomanager.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "packages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PackageMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String version;

    private String author;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String dependenciesJson;

    private String filePath;

}
