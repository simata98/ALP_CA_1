package com.ktproject.backend.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ktproject.backend.dto.SaveDBRequest;
import com.ktproject.backend.service.SearchService;

@RestController
@RequestMapping("/api")
public class SearchController {
  @Autowired
  private SearchService searchService;

  @PostMapping("/save-db")
  public ResponseEntity<?> saveDbInfo(@RequestBody SaveDBRequest request) {
    searchService.saveDbInfo(request.getDbName(), request.getUserName());
    return ResponseEntity.ok().build();
  }

  @GetMapping("/get-urls/{username}")
  public ResponseEntity<List<String>> getUserUrls(@PathVariable String username) {
    List<String> urls = searchService.getUserUrls(username);
    return ResponseEntity.ok(urls);
  }
}
