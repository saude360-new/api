package com.smarthealth.io.smarthealth.shared.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


// Provides a way to handle abstract variables (such as env vars)
public class AbstractVariablesResolverService {
    private static Path findProjectRoot(Path directory) {
    try(DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
      for(Path file : stream) {
        if(Files.isDirectory(file)) {
          Path found = findProjectRoot(file);

          if(found != null)
            return found;
          
        } else if (file.getFileName().toString().equals("pom.xml"))
          return directory;
      }
    } catch (IOException e) {
      throw new RuntimeException("Error reading directory: " + directory, e);
    }

    return null;
  }

  protected static Path getEnvFilePath() throws RuntimeException {
    Path currentDir = Paths.get("").toAbsolutePath();
    Path projectRoot = findProjectRoot(currentDir);

    if(projectRoot != null) {
      Path envFilePath = projectRoot.resolve(".env");

      if(Files.exists(envFilePath))
        return envFilePath;

      throw new RuntimeException("Error: .env file not found in project root: " + projectRoot);
    }

    throw new RuntimeException("Error: pom.xml not found, not in a Maven project.");
  }

  private final Map<String, String> environmentVariables = new HashMap<>();

  public AbstractVariablesResolverService() throws RuntimeException {
    Path envFilePath = AbstractVariablesResolverService.getEnvFilePath();

    if(!Files.exists(envFilePath)) {
      throw new RuntimeException("Error: .env file not found at " + envFilePath);
    }

    try(BufferedReader reader = Files.newBufferedReader(envFilePath)) {
      String line;

      while((line = reader.readLine()) != null) {
        line = line.trim();

        if(line.isEmpty() || line.startsWith("#") || line.startsWith(";"))
          continue;

        int delimiterIndex = line.indexOf("=");

        if(delimiterIndex < 0)
          continue;

        String key = line.substring(0, delimiterIndex).trim();
        String value = line.substring(delimiterIndex + 1).trim();

        // Remove surrounding quotes if present
        if((value.startsWith("\"") && value.endsWith("\"")) || (value.startsWith("'") && value.endsWith("'"))) {
          value = value.substring(1, value.length() - 1);
        }

        environmentVariables.put(key, value);
      }
    } catch (IOException e) {
      throw new RuntimeException("Error reading .env file: " + e.getMessage(), e);
    }
  }

  public Option<String> getEnvironmentVariable(String varname) {
    if (this.environmentVariables.containsKey(varname))
      return Option.some(this.environmentVariables.get(varname));

    return Option.none();
  }
}
