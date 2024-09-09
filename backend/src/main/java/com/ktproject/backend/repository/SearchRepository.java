package com.ktproject.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ktproject.backend.entity.Searchs;

@Repository
public interface SearchRepository extends JpaRepository<Searchs, Long> {
}
