package com.ktproject.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ktproject.backend.dto.QueryRequestDTO;
import com.ktproject.backend.service.SqlAssistantService;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api")
public class SqlAssistantController {
  @Autowired
  private SqlAssistantService sqlAssistantService;

  @Autowired
  private ObjectMapper objectMapper;

  @PostMapping("/executeQuery")
  public ObjectNode executeQuery(@RequestBody QueryRequestDTO queryRequestDTO) {
    String dbName = queryRequestDTO.getDbName();
    String inputQuery = queryRequestDTO.getInputQuery();
    String result = sqlAssistantService.executePythonScript(dbName, inputQuery);

    ObjectNode jsonResponse = objectMapper.createObjectNode();
    jsonResponse.put("result", result);
    return jsonResponse;
  }
}
