package com.trello.backend.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String taskID;
    private String ref;
    private String name;
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private BoardList boardList;


    @OneToMany(mappedBy = "task",cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Attachment> attachments = new ArrayList<>();
}
