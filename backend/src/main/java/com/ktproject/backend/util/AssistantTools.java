package com.ktproject.backend.util;

import org.springframework.stereotype.Component;

import dev.langchain4j.agent.tool.Tool;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.time.LocalTime;

@Component
public class AssistantTools {
  @Tool
  String currentTime() {
    return LocalTime.now().toString();
  }

  public static PathMatcher glob(String glob) {
    return FileSystems.getDefault().getPathMatcher("glob:" + glob);
  }

  public static Path toPath(String relativePath) {
    try {
      URL fileUrl = AssistantTools.class.getClassLoader().getResource(relativePath);
      return Paths.get(fileUrl.toURI());

    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }
}
