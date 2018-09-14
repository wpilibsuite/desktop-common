package edu.wpi.first.desktop.settings;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A category of settings. This is typically used to contain all the settings for a single configurable object, such
 * as a component, layout, or tab.
 */
public final class Category {

  private final String name;
  private final List<Category> subcategories;
  private final List<Group> groups;

  /**
   * Creates a new category of settings.
   *
   * @param name   the name of the category
   * @param groups the groups of settings in this category
   *
   * @return a new category
   */
  public static Category of(String name, Group... groups) {
    return Category.of(name, List.of(groups));
  }

  /**
   * Creates a new category of settings.
   *
   * @param name   the name of the category
   * @param groups the groups of settings in this category
   *
   * @return a new category
   */
  public static Category of(String name, List<Group> groups) {
    return new Category(name, List.of(), Collections.unmodifiableList(groups));
  }

  /**
   * Creates a new category of settings, with optional subcategories.
   *
   * @param name          the name of the category
   * @param subcategories the subcategories underneath this one
   * @param groups        the groups of settings in this category
   *
   * @return a new category
   */
  public static Category of(String name, List<Category> subcategories, List<Group> groups) {
    return new Category(name, Collections.unmodifiableList(subcategories), Collections.unmodifiableList(groups));
  }

  private Category(String name, List<Category> subcategories, List<Group> groups) {
    Objects.requireNonNull(name, "A category name cannot be null");
    if (name.chars().allMatch(Character::isWhitespace)) {
      throw new IllegalArgumentException("A category name cannot be empty");
    }
    this.name = name;
    this.subcategories = subcategories;
    this.groups = groups;
  }

  /**
   * Gets the name of this category.
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the subcategories below this one. This list is read-only.
   */
  public List<Category> getSubcategories() {
    return subcategories;
  }

  /**
   * Gets the groups of settings in this category. This list is read-only.
   */
  public List<Group> getGroups() {
    return groups;
  }

}
