package com.trello.backend.demo.service;

import com.trello.backend.demo.entity.Attachment;
import com.trello.backend.demo.entity.Task;
import com.trello.backend.demo.repo.FileRepository;
import com.trello.backend.demo.repo.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class FilesUploadStorageServiceImp implements FilesUploadStorageService {

    private final Path root = Paths.get("uploads");

    FileRepository fileRepository;
    TaskRepository taskRepository;

    @Autowired
    public FilesUploadStorageServiceImp(FileRepository fileRepository, TaskRepository taskRepository) {
        this.fileRepository = fileRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public void init() {
        try {
            System.out.println("path : " + root);
            Files.createDirectory(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public Attachment save(MultipartFile file , String taskID) {
        try {
            Task task = taskRepository.findByTaskID(taskID);
            Attachment new_attachment =new Attachment();
            new_attachment.setFileID(UUID.randomUUID().toString());
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            Path path = root.resolve(fileName);
            Resource resource = new UrlResource(path.toUri());
            String URI = resource.getURL().toString();

            new_attachment.setName(fileName);
            new_attachment.setType(file.getContentType());
            new_attachment.setPath(path.toString());
            new_attachment.setUri(URI);
            new_attachment.setTask(task);

            Attachment returnedData = fileRepository.save(new_attachment);
            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));

            return returnedData;
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    @Override
    public List<Attachment> loadAttachedFiles(String TaskID) {
        try {
            Task task = taskRepository.findByTaskID(TaskID);
            List<Attachment> attachmentList = new ArrayList<>();
            if(task != null){
                attachmentList = fileRepository.findAttachmentsByTask_TaskID(TaskID);
            }
            return attachmentList;
        } catch (Exception e) {
            throw new RuntimeException("Could not retrieve the files. Error: " + e.getMessage());
        }
    }

    @Override
    public Boolean DeleteAttachedFile(String fileID) throws IOException {
        Attachment attachment = fileRepository.findByFileID(fileID);
        String filename = attachment.getName();
        Path file =root.resolve(filename);
        FileSystemUtils.deleteRecursively(file);
        fileRepository.delete(attachment);
        return true;
    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }
}
