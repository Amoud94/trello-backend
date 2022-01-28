package com.trello.backend.demo.service;

import com.trello.backend.demo.entity.Attachment;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FilesUploadStorageService {

    void init();
    void deleteAll();

    Attachment save(MultipartFile file, String TaskID);
    List<Attachment> loadAttachedFiles(String TaskID);
    Boolean DeleteAttachedFile(String fileID) throws IOException;
    Resource load(String filename);
}
