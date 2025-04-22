package com.repsy.repomanager.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String saveFile(String filename, MultipartFile file);
    Resource loadFile(String filename);
}
