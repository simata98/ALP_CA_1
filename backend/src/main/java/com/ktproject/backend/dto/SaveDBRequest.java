package com.ktproject.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveDBRequest {
  private String dbName;
  private String userName;
}
