package com.trello.backend.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;
  @Enumerated(EnumType.STRING)
  private ERole name;

  public Role() { }

  public Role(ERole name) {
    this.name = name;
  }

}
