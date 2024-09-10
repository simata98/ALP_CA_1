package com.ktproject.backend.service;

import java.util.Optional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ktproject.backend.entity.Searchs;
import com.ktproject.backend.entity.User;
import com.ktproject.backend.repository.SearchRepository;
import com.ktproject.backend.repository.UserRepository;

@Service
public class SearchService {
    @Autowired
    private SearchRepository searchRepository;

    @Autowired
    private UserRepository userRepository;

    public void saveDbInfo(String dbName, String username) {
      // 먼저 UserRepository에서 username으로 User를 찾음
      Optional<User> userOptional = userRepository.findByUsername(username);

      if (userOptional.isPresent()) {
        User user = userOptional.get();

        // 해당 User와 연관된 Searchs 레코드를 검색
        Searchs searchs = searchRepository.findByUsers(user);

        if (searchs == null) {
          // 검색 결과가 없으면 새로운 Searchs 생성
          searchs = new Searchs();
          searchs.setUsers(user); // User와 연관 설정
        }

        // 현재 URL들 중 빈 곳이 있는지 확인하고 없으면 순환적으로 덮어씀
        if (searchs.getUrl1() == null || searchs.getUrl1().isEmpty()) {
          searchs.setUrl1(dbName);
        } else if (searchs.getUrl2() == null || searchs.getUrl2().isEmpty()) {
          searchs.setUrl2(dbName);
        } else if (searchs.getUrl3() == null || searchs.getUrl3().isEmpty()) {
          searchs.setUrl3(dbName);
        } else if (searchs.getUrl4() == null || searchs.getUrl4().isEmpty()) {
          searchs.setUrl4(dbName);
        } else if (searchs.getUrl5() == null || searchs.getUrl5().isEmpty()) {
          searchs.setUrl5(dbName);
        } else {
          // 모든 URL이 차있는 경우, 순환하여 url1부터 덮어씌움
          searchs.setUrl1(dbName);
          searchs.setUrl2(dbName);
          searchs.setUrl3(dbName);
          searchs.setUrl4(dbName);
          searchs.setUrl5(dbName);
        }

        // Searchs 레코드를 저장
        searchRepository.save(searchs);
      } else {
        // User가 존재하지 않으면 예외 처리 또는 로깅
        throw new RuntimeException("User not found with username: " + username);
      }
    }
    
    public List<String> getUserUrls(String username) {
      // UserRepository에서 username으로 User를 찾음
      Optional<User> userOptional = userRepository.findByUsername(username);

      if (userOptional.isPresent()) {
          User user = userOptional.get();
          // 해당 User와 연관된 Searchs 레코드를 검색
          Searchs searchs = searchRepository.findByUsers(user);

          if (searchs != null) {
              // url1~url5 중 null이 아닌 값만 리스트에 추가
              List<String> urls = new ArrayList<>();
              if (searchs.getUrl1() != null) urls.add(searchs.getUrl1());
              if (searchs.getUrl2() != null) urls.add(searchs.getUrl2());
              if (searchs.getUrl3() != null) urls.add(searchs.getUrl3());
              if (searchs.getUrl4() != null) urls.add(searchs.getUrl4());
              if (searchs.getUrl5() != null) urls.add(searchs.getUrl5());
              
              return urls;
          }
      }
      return Collections.emptyList();
  }
}