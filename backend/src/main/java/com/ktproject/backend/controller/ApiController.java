/*
 * Rest API 처리용 컨트롤러
 * js 혹은 vue와 통신 담당용
 */

package com.ktproject.backend.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ktproject.backend.dto.SalesGroupBy;
import com.ktproject.backend.service.SalesService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class ApiController {
  @Autowired
  private SalesService salesService;

  @GetMapping("/sales")
  //Swagger API 문서화
  @Tag(name = "세일즈 데이터 조회", description = "판매 데이터를 조회합니다.")
  @Operation(summary = "카테고리 개수, 평균 가격/개수 조회", description = "카테고리 개수, 평균 가격/개수 조회")
  public List<SalesGroupBy> sales() {
    // 별도의 비즈니스 로직 없음
    return salesService.getSalesGroupByData();
  }
}
