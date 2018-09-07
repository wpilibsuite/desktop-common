package edu.wpi.first.desktop.util;

/**
 * A base class for utility classes. The only constructor is {@code protected} and will always throw a
 * {@link UnsupportedOperationException} if called.
 */
public abstract class UtilityClass {

  protected UtilityClass() {
    throw new UnsupportedOperationException(getClass().getName() + " is a utility class and cannot be instantiated");
  }
}
