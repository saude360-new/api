package com.smarthealth.io.smarthealth.shared.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
// import java.io.InvalidObjectException;


public class EmailAddress {
  private String value;

  private EmailAddress(String value) {
    this.value = value;
  }

  public static boolean validate(String value) {
    final String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    final Matcher match = Pattern.compile(emailPattern).matcher(value);

    return match.matches();
  }

  // TODO: "create" method
}
