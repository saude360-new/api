package com.smarthealth.io.smarthealth.shared.lib;

import java.sql.*;
import com.smarthealth.io.smarthealth.shared.core.AbstractVariablesResolverService;


public class DatabaseConnection {
  private final AbstractVariablesResolverService env;
  private final Connection client;
  private boolean disposed = false;

  public DatabaseConnection() throws SQLException {
    this.env = new AbstractVariablesResolverService();

    final String postgresUrl = this.env
      .getEnvironmentVariable("POSTGRES_URL")
      .unwrapExpect("Unable to find database url");

    final String username = this.env
      .getEnvironmentVariable("DATABASE_USER")
      .unwrapExpect("Unable to find database username");

    final String password = this.env
      .getEnvironmentVariable("DATABASE_PASS")
      .unwrapExpect("Unable to find database password");


    this.client = DriverManager.getConnection(postgresUrl, username, password);
  }

  public Connection getConnection() throws RuntimeException {
    if(this.disposed) {
      throw new RuntimeException("This database connection is already disposed");
    }

    return this.client;
  }

  public void dispose() throws SQLException {
    if(this.disposed)
      return;

    this.client.close();
    this.disposed = true;
  }
}
