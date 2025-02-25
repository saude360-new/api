package com.smarthealth.io.smarthealth.shared.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

// Provides a way to handle abstract variables (such as env vars)
public class AbstractVariablesResolverService {
  private final Map<String, String> environmentVariables = new HashMap<>();

  public AbstractVariablesResolverService() throws RuntimeException {
    String envFileName = ".env";

    try(InputStream inputStream = getClass().getClassLoader().getResourceAsStream(envFileName)) {
      if(inputStream == null) {
        throw new RuntimeException("Error: .env file not found in resources: " + envFileName);
      }

      try(BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
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
