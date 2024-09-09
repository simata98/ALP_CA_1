package com.ktproject.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QueryRequestDTO {
  private String dbName;
  private String inputQuery;
}
