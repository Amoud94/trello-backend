package com.trello.backend.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
public class BoardDTO {
    private String boardID;
    private String ref;
    private String name;
    private String appUser_ID;
}
