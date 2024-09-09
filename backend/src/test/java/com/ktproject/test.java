package com.ktproject;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.Test;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.stream.LogOutputStream;

public class test {
  @Test
  public static void main(String[] args) {
    try {
      String pythonPath = "/opt/miniconda3/envs/langchain/bin/python";
      String scriptPath = "backend/src/main/java/com/ktproject/backend/service/python/SQL_Database_test.py";
      
      // 사용자 입력 DB_NAME 설정
      String dbName = "kt_db";
      String inputQuery = "차를 구매한 남녀 성비 비율을 알려줘";

      new ProcessExecutor().command(pythonPath, scriptPath, dbName, inputQuery)
      .redirectOutput(new LogOutputStream() {
        @Override
        protected void processLine(String line) {
          System.out.println(line);
        }
          })
          .redirectError(new LogOutputStream() {
            @Override
            protected void processLine(String line) {
              System.out.println("Error" + line);
            }
          })
          .execute();
    } catch (IOException | InterruptedException | TimeoutException e){
      e.printStackTrace();
    }
  }
}
