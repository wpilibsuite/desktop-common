package edu.wpi.first.desktop.settings;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A group of {@link Setting Settings}.
 */
public final class Group {

  private final String name;
  private final List<Setting<?>> settings;

  /**
   * Creates a new group of settings.
   *
   * @param name     the name of the group
   * @param settings the settings in the group
   *
   * @return a new group
   */
  public static Group of(String name, Setting<?>... settings) {
    return new Group(name, List.of(settings));
  }

  /**
   * Creates a new group of settings.
   *
   * @param name     the name of the group
   * @param settings the settings in the group
   *
   * @return a new group
   */
  public static Group of(String name, List<Setting<?>> settings) {
    return new Group(name, Collections.unmodifiableList(settings));
  }

  private Group(String name, List<Setting<?>> settings) {
    Objects.requireNonNull(name, "A group name cannot be null");
    if (name.chars().allMatch(Character::isWhitespace)) {
      throw new IllegalArgumentException("A group name cannot be empty");
    }
    this.name = name;
    this.settings = settings;
  }

  /**
   * Gets the name of this group.
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the settings in this group. This list is read-only.
   */
  public List<Setting<?>> getSettings() {
    return settings;
  }
}
