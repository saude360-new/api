/*package com.smarthealth.io.smarthealth;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import com.smarthealth.io.smarthealth.shared.core.Option;
import com.smarthealth.io.smarthealth.shared.core.AbstractVariablesResolverService;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.smarthealth.io.smarthealth.SmarthealthApplication;



//@SpringBootTest
@ActiveProfiles("test")
class SmarthealthApplicationTests {

  public static void main(String[] args) throws IllegalArgumentException, Exception {
    final SmarthealthApplicationTests optionalTester = new SmarthealthApplicationTests();

    optionalTester.testSome();
    optionalTester.testNone();
    optionalTester.testOptionalCatch();
    optionalTester.testOptionalResolve();
    optionalTester.testUnwrapExpect();

    final SmarthealthApplicationTests environmentTester = new SmarthealthApplicationTests();

    environmentTester.testVariablesResolver();
  }

  @Test
  void contextLoads() { }

  @Test
  void testVariablesResolver() {
    final AbstractVariablesResolverService env = new AbstractVariablesResolverService();
    
    System.out.println(String.format("EN=%s", env.getEnvironmentVariable("EN").unwrap()));
    assertEquals(env.getEnvironmentVariable("EN").unwrap(), "VAR");
  }

  @Test
  void testSome() {
    Option<String> option = Option.some("Hello");
    assertTrue(option.isSome());
    assertFalse(option.isNone());
    assertEquals("Hello", option.unwrap());
    assertEquals("Hello", option.unwrapOr("Fallback"));
  }

  @Test
  void testNone() {
    Option<String> option = Option.none();
    assertFalse(option.isSome());
    assertTrue(option.isNone());
    assertEquals("Fallback", option.unwrapOr("Fallback"));

    Exception exception = assertThrows(RuntimeException.class, option::unwrap);
    assertEquals("Cannot unwrap a None value", exception.getMessage());
  }

  @Test
  void testUnwrapExpect() {
    Option<String> option = Option.none();
    Exception exception = assertThrows(RuntimeException.class, () -> option.unwrapExpect("Custom error"));
    assertEquals("Custom error", exception.getMessage());
  }

  @Test
  void testOptionalCatch() {
    Option<Integer> option = Option.optionalCatch(() -> Integer.parseInt("123"));
    assertTrue(option.isSome());
    assertEquals(123, option.unwrap());

    Option<Integer> failedOption = Option.optionalCatch(() -> Integer.parseInt("abc"));
    assertTrue(failedOption.isNone());
  }

  @Test
  void testOptionalResolve() {
    Optional<String> someValue = Optional.of("Hello");
    Option<String> option = Option.optionalResolve(someValue);
    assertTrue(option.isSome());
    assertEquals("Hello", option.unwrap());

    Optional<String> noneValue = Optional.empty();
    Option<String> noneOption = Option.optionalResolve(noneValue);
    assertTrue(noneOption.isNone());
  }
}
*/