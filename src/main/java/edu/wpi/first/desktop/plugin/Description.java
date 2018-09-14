package edu.wpi.first.desktop.plugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Describes a plugins group ID (a unique identifier for the group or organization that develops the plugin); and its
 * name, version, and a summary of what the plugin provides.
 *
 * <p><strong>This annotation <i>must</i> be present on an annotated plugin class, or it will not be able to be loaded.
 * </strong>
 *
 * <p>Note that the version <i>must</i> follow <a href="http://semver.org">semantic versioning</a> guidelines.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Description {

  /**
   * The group ID of the plugin owner, in reverse domain name notation e.g. {@code "com.example.my.plugin"}
   */
  String groupId();

  /**
   * The name of the plugin.
   */
  String name();

  /**
   * The current version of the plugin.
   */
  String version();

  /**
   * A summary of what the plugin provides.
   */
  String summary();
}
