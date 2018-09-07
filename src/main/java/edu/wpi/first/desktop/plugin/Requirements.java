package edu.wpi.first.desktop.plugin;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a plugin class as requiring other plugins. This used so that the requirements exist on the class-level,
 * allowing plugins to reference classes or types not present on the application classpath at load time.
 *
 * <p>Rather than using this annotation directly, the {@link Requires @Requires} annotation can be used multiple
 * times on a single class, reducing clutter.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Requirements {
  Requires[] value();
}
