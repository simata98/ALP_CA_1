package com.ktproject.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ktproject.backend.util.ResCode;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetAboutRespDto {
  private String str;
  private int code = ResCode.SUCCESS.value();
  private String message;
}
