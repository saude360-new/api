package com.smarthealth.io.smarthealth.shared.core;

import java.util.regex.Pattern;


public class QueryBuilder {
  private final String table;
  private final String alias;
  private final Pattern rexp = Pattern.compile("^[A-Za-z0-9_-]+$");

  private String operation = null;
  private String query = "";

  public QueryBuilder(String tableName) throws IllegalArgumentException {
    if (!rexp.matcher(tableName).matches()) {
      throw new IllegalArgumentException("The table name has some invalid characters");
    }

    this.table = tableName;
    this.alias = new StringBuilder().append(tableName.charAt(0)).toString();
  }

  public QueryBuilder(String tableName, String tableAlias) throws IllegalArgumentException {
    if (!rexp.matcher(tableName).matches()) {
      throw new IllegalArgumentException("The table name has some invalid characters");
    }

    if (!rexp.matcher(tableAlias).matches()) {
      throw new IllegalArgumentException("The table name has some invalid characters");
    }

    this.table = tableName;
    this.alias = tableAlias;
  }

  public QueryBuilder select(String wildcard) throws Exception, IllegalArgumentException {
    this.ensureOperationsIsNotIntitialized();

    if (wildcard != "*") {
      throw new Exception("When you use wildcard operator '*' it must be a string with only '*' operator");
    }

    this.operation = "SELECT";
    this.query = String.format("SELECT * FROM %s %s", this.table, this.alias);

    return this;
  }

  public QueryBuilder select(String[] columns) throws Exception, IllegalArgumentException {
    this.ensureOperationsIsNotIntitialized();

    this.operation = "SELECT";
    this.query = "SELECT ";

    if (columns[0] == "*" && columns.length > 1) {
      throw new Exception("When you use wildcard operator '*' it must be the only argument in the columns list");
    }

    for (final String column : columns) {
      if (!this.rexp.matcher(column).matches()) {
        throw new IllegalArgumentException("The column name has some invalid characters");
      }
    }

    query += String.join(", ", columns);
    query += String.format(" FROM %s %s", this.table, this.alias);

    return this;
  }

  public QueryBuilder where(String rawExpression) throws Exception {
    if(this.operation == null || (!this.operation.equals("SELECT") && !this.operation.equals("UPDATE") && !this.operation.equals("DELETE"))) {
      throw new Exception("You can only use 'WHERE' clause with a select, update, or delete operation");
    }

    String expression = rawExpression.trim();

    if(expression.toUpperCase().startsWith("WHERE")) {
      expression = expression.substring(5).trim();
    }

    this.query += String.format(" WHERE %s", expression);
    return this;
  }

  public String getTable() {
    return new String(table.getBytes());
  }

  public String toString() {
    String result = new String(query.getBytes()).trim();

    if (result.endsWith(";"))
      return result;

    return String.format("%s;", result);
  }

  private void ensureOperationsIsNotIntitialized() throws Exception {
    if (this.operation != null) {
      throw new Exception(String.format("This instance of query builder is already initialized with a %s operation",
          this.operation.toUpperCase()));
    }
  }
}
