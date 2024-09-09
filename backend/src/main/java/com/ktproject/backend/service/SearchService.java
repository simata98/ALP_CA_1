package com.ktproject.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ktproject.backend.entity.Searchs;
import com.ktproject.backend.entity.User;
import com.ktproject.backend.repository.SearchRepository;

@Service
public class SearchService {
  @Autowired
  private SearchRepository searchRepository;

  public void saveDbInfo(String dbName, User user) {
    Searchs searchs = new Searchs();

    searchs.setUrl1(dbName + "_url1");
    searchs.setUrl2(dbName + "_url2");
    searchs.setUrl3(dbName + "_url3");
    searchs.setUrl4(dbName + "_url4");
    searchs.setUrl5(dbName + "_url5");

    searchRepository.save(searchs);
  }
}
