package com.trello.backend.demo.model.request;

import lombok.Data;

@Data
public class UpdateTaskStatus {
    private String id;
    private String name;
    private String ref;
    private String status;
    private String taskID;

}
