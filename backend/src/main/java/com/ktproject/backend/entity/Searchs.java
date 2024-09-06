package com.ktproject.backend.entity;

import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "searchs")
@NoArgsConstructor
@AllArgsConstructor
public class Searchs {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Integer search_cnt;
  // SQL 접속 URL을 5개까지 저장가능하도록
  private String url1;
  private String url2;
  private String url3;
  private String url4;
  private String url5;

  // user_id를 외래키로 사용
  @OneToOne
  @JoinColumn(name = "user_id")
  private User users;

}
