package com.ktproject.backend.dto;

import com.ktproject.backend.entity.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveDBRequest {
  private String dbName;
  private User user;
}
