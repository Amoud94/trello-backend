package com.trello.backend.demo.model.request;

import lombok.Data;

@Data
public class AddTask {
//    private String list_ID;
    private String ref;
    private String name;
    private String status;
}
