package edu.wpi.first.desktop.util;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Utilities dealing with the type system.
 */
public final class TypeUtils extends UtilityClass {

  /**
   * If 'value' can be cast into 'cls', returns an Optional of that casted value. Otherwise, returns Empty.
   */
  public static <T> Optional<T> optionalCast(Object value, Class<T> cls) {
    return cls.isAssignableFrom(value.getClass())
        ? Optional.of(cls.cast(value))
        : Optional.empty();
  }

  public static <T> Function<Object, Optional<T>> optionalCast(Class<T> cls) {
    return value -> optionalCast(value, cls);
  }

  /**
   * <p>Filter out members of a subtype from a stream of some base type.
   * For example, this code:</p>
   *
   * <pre>{@code
   *   getFoos().stream()
   *     .filter(f -> f instanceof Bar)
   *     .map(f -> (Bar) f)
   *     .forEach(b -> b.doBar())
   * }</pre>
   *
   * <p>can be turned into:</p>
   *
   * <pre>{@code
   *   getFoos().stream()
   *     .flatMap(TypeUtils.castStream(Bar.class))
   *     .forEach(b -> b.doBar())
   * }</pre>
   */
  public static <T, U extends T> Function<T, Stream<U>> castStream(Class<U> cls) {
    return value -> cls.isAssignableFrom(value.getClass())
        ? Stream.of(cls.cast(value))
        : Stream.empty();
  }
}
