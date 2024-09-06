package com.ktproject.backend.util;

public enum ResCode {
  SUCCESS(0);

  private final int value;

  ResCode(int value) {
    this.value = value;
  }

  public int value() {
    return value;
  }
}
