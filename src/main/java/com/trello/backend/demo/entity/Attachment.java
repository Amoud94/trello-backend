package com.trello.backend.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String fileID;
    private String name;
    private String type;
    private String path;
    private String uri;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Task task;

}
