package com.ktproject.backend.service;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ktproject.backend.dto.SalesGroupBy;
import com.ktproject.backend.dto.SalesGroupByInter;
import com.ktproject.backend.repository.SalesRepository;

@Service
public class SalesService {
  @Autowired
  private SalesRepository salesRepository;

  public List<SalesGroupBy> getSalesGroupByData() {
    // 1. 결과 쿼리 수행수 결과 획득
    List<SalesGroupByInter> res = salesRepository.findGroupBySalesData();
    // 2. 결과 -> 형식 교환할 리스트 준비
    List<SalesGroupBy> results = new ArrayList<SalesGroupBy>();
    // 3. 형식 교환후 리스트에 추가
    for (SalesGroupByInter r : res) {
      results.add(SalesGroupBy.builder()
      .category(r.getCategory())
      .cnt(r.getCnt())
      .price(r.getPrice())
      .amount(r.getAmount())
      .build()
      );
    }
    // 4. 리스트 반환
    return results;
  }
}
