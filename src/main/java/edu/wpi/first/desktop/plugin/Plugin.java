package edu.wpi.first.desktop.plugin;

import java.util.Collection;

/**
 * Common interface for plugins that can be loaded by the application at startup or at runtime. A plugin that depends
 * on another should <i>never</i> reference classes from that plugin in fields or method return types in the plugin
 * class, since there is no guarantee that classes provided by the dependency have been loaded at the time of plugin
 * resolution.
 *
 * @param <T> the type of object the plugin applies to
 */
public interface Plugin<T> {

  /**
   * Applies this plugin to the target object.
   *
   * @param target the target to apply this plugin to
   */
  void applyTo(T target);

  /**
   * Removes this plugin from the target. After this method is called, the target <i>must</i> be in the same state
   * as if this plugin were never applied in the first place.
   *
   * @param target the target to remove this plugin from
   */
  void removeFrom(T target);

  /**
   * Gets a description of this plugin.
   *
   * @return a description of this plugin
   */
  Descriptor descriptor();

  /**
   * Gets the requirements for all the plugins required by this one.
   */
  Collection<Requirement> requirements();

  /**
   * Checks if this plugin directly depends on another. This does <i>not</i> check for transitive dependencies.
   *
   * @param other the plugin to check
   *
   * @return true if this plugin depends on {@code other}, false if it does not
   */
  default boolean dependsOn(Plugin<T> other) {
    return requirements().stream().anyMatch(r -> r.matches(other.descriptor()));
  }
}
