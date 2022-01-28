package com.trello.backend.demo.controller;

import com.trello.backend.demo.entity.Attachment;
import com.trello.backend.demo.service.FilesUploadStorageService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api")
public class FilesUploadController {

    @Autowired
    FilesUploadStorageService filesUploadStorageService;

    @PostMapping("/tasks/{id}/attachments")
    public ResponseEntity<Map<String,Attachment>> uploadFiles(@PathVariable(name = "id") String TaskID, @RequestPart("files") MultipartFile files) {
        Map<String,Attachment> response = new HashMap<>();
        try {
            response.put("ReturnedData",filesUploadStorageService.save(files, TaskID));
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("Fail to upload attached files!", null);
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(response);
        }
    }

    @GetMapping("/tasks/{id}/attachments")
    public ResponseEntity<Map<String,List<Attachment>>> getTaskAttachedFiles(@PathVariable(name = "id") String TaskID ){
        Map<String,List<Attachment>> response = new HashMap<>();
        try {
            List<Attachment> attachments = filesUploadStorageService.loadAttachedFiles(TaskID);
            response.put("ReturnedData", attachments);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("Fail to download the attached files!",new ArrayList());
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(response);
        }
    }
    @DeleteMapping("/tasks/attachments/{id}")
    public ResponseEntity removeBoardList(@PathVariable(value = "id") String attachmentID) throws IOException {
        if(filesUploadStorageService.DeleteAttachedFile(attachmentID)) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/tasks/attachments/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = filesUploadStorageService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
}
