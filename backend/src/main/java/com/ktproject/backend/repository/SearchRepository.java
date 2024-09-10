package com.ktproject.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ktproject.backend.entity.Searchs;
import com.ktproject.backend.entity.User;

public interface SearchRepository extends JpaRepository<Searchs, Long> {
  Searchs findByUsers(User users);  // User 엔티티로 검색
}