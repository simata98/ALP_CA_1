package com.ktproject.backend.service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.stream.LogOutputStream;

@Service
public class SqlAssistantService {
  // setting path and script path
  @Value("${PYTHON_PATH}")
  private String pythonPath;
  @Value("${SCRIPT_PATH}")
  private String scriptPath;

  public String executePythonScript(String dbName, String inputQuery) {
    try {
      StringBuilder output = new StringBuilder();

      new ProcessExecutor().command(pythonPath, scriptPath, dbName, inputQuery)
          .redirectOutput(new LogOutputStream() {
            @Override
            protected void processLine(String line) {
              output.append(line).append("/n");
            }
          })
          .redirectError(new LogOutputStream() {
            @Override
            protected void processLine(String line) {
              output.append("Error: ").append(line).append("/n");
            }
          })
          .execute();
      return output.toString();
    } catch (IOException | InterruptedException | TimeoutException e) {
      e.printStackTrace();
      return "Error: " + e.getMessage();
    }
  }
}
