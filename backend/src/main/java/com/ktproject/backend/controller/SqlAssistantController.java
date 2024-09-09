package com.ktproject.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ktproject.backend.dto.QueryRequestDTO;
import com.ktproject.backend.service.SqlAssistantService;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api")
public class SqlAssistantController {
  @Autowired
  private SqlAssistantService sqlAssistantService;

  @PostMapping("/executeQuery")
  public String executeQuery(@RequestBody QueryRequestDTO queryRequestDTO) {
    String dbName = queryRequestDTO.getDbName();
    String inputQuery = queryRequestDTO.getInputQuery();
    System.out.println(dbName);
    System.out.println(inputQuery);
    return sqlAssistantService.executePythonScript(dbName, inputQuery);
  }
}
