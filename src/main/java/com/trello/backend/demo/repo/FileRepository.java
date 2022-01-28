package com.trello.backend.demo.repo;

import com.trello.backend.demo.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<Attachment,String>{
    Attachment findByFileID(String fileID);
    List<Attachment> findAttachmentsByTask_TaskID(String taskID);
}
