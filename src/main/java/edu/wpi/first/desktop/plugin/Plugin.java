package edu.wpi.first.desktop.plugin;

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
}
