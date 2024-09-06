package com.ktproject.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
@Entity
public class Sales {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer order_id;
  private LocalDateTime date;
  private String category;
  private Integer item_code;
  private Integer price;
  private Integer amount;
}
