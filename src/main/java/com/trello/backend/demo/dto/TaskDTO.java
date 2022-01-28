package com.trello.backend.demo.dto;

import lombok.Data;


@Data
public class TaskDTO {
    private String taskID;
    private String list_ID;
    private String ref;
    private String name;
    private String status;

}
