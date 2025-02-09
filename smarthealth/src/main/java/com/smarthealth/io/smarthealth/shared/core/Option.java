package com.smarthealth.io.smarthealth.shared.core;

import java.util.Optional;
import java.util.function.Supplier;


public abstract class Option<T> {

  public abstract boolean isSome();

  public abstract boolean isNone();

  public abstract T unwrap();

  public abstract T unwrapOr(T fallback);

  public abstract T unwrapExpect(String message);

  public static <T> Option<T> some(T value) {
    return new Some<>(value);
  }

  public static <T> Option<T> none() {
    return new None<>();
  }

  private static class Some<T> extends Option<T> {
    private final T value;

    private Some(T value) {
      this.value = value;
    }

    @Override
    public boolean isSome() {
      return true;
    }

    @Override
    public boolean isNone() {
      return false;
    }

    @Override
    public T unwrap() {
      return value;
    }

    @Override
    public T unwrapOr(T fallback) {
      return value;
    }

    @Override
    public T unwrapExpect(String message) {
      return value;
    }
  }

  private static class None<T> extends Option<T> {

    private None() { }

    @Override
    public boolean isSome() {
      return false;
    }

    @Override
    public boolean isNone() {
      return true;
    }

    @Override
    public T unwrap() {
      throw new RuntimeException("Cannot unwrap a None value");
    }

    @Override
    public T unwrapOr(T fallback) {
      return fallback;
    }

    @Override
    public T unwrapExpect(String message) {
      throw new RuntimeException(message);
    }
  }

  public static <T> Option<T> optionalCatch(Supplier<T> fn) {
    try {
      return some(fn.get());
    } catch (Exception e) {
      return none();
    }
  }

  public static <T> Option<T> optionalResolve(Optional<T> optional) {
    return optional.map(Option::some).orElseGet(Option::none);
  }
}
