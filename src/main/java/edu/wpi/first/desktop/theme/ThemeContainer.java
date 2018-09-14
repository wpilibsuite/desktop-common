package edu.wpi.first.desktop.theme;

import java.util.Objects;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public final class ThemeContainer {

  private final ObservableList<Theme> themes = FXCollections.observableArrayList();
  private final ObservableList<Theme> themesUnmodifiable = FXCollections.unmodifiableObservableList(themes);

  public ThemeContainer() {
    themes.add(Theme.MODENA);
  }

  /**
   * Adds a theme to this container.
   *
   * @param theme the theme to add
   *
   * @throws NullPointerException if the theme is null
   */
  public void addTheme(Theme theme) {
    Objects.requireNonNull(theme, "Theme cannot be null");
    themes.add(theme);
  }

  /**
   * Adds themes to this container.
   *
   * @param themes the themes to add
   *
   * @throws NullPointerException if {@code themes} is null or contains a null element
   */
  public void addThemes(Theme... themes) {
    for (Theme theme : themes) {
      addTheme(theme);
    }
  }

  /**
   * Adds themes to this container.
   *
   * @param themes the themes to add
   *
   * @throws NullPointerException if {@code themes} is null or contains a null element
   */
  public void addThemes(Iterable<Theme> themes) {
    for (Theme theme : themes) {
      addTheme(theme);
    }
  }

  /**
   * Removes a theme from this container.
   *
   * @param theme the theme to remove
   */
  public void removeTheme(Theme theme) {
    themes.remove(theme);
  }

  /**
   * Removes themes from this container.
   *
   * @param themes the themes to remove
   */
  public void removeThemes(Theme... themes) {
    for (Theme theme : themes) {
      removeTheme(theme);
    }
  }

  /**
   * Removes themes from this container.
   *
   * @param themes the themes to remove
   */
  public void removeThemes(Iterable<Theme> themes) {
    for (Theme theme : themes) {
      removeTheme(theme);
    }
  }

  /**
   * Gets a read-only observable list of the themes in this container.
   *
   * @return the themes in this container
   */
  public ObservableList<Theme> getThemes() {
    return themesUnmodifiable;
  }

  /**
   * Gets the theme in this container with the specified name.
   *
   * @param name the name of the theme to get. This is case-sensitive.
   *
   * @return an optional of the theme in this container with the specified name, or an empty optional if no such theme
   *        is present
   */
  public Optional<Theme> getTheme(String name) {
    return themes.stream()
        .filter(t -> t.getName().equals(name))
        .findFirst();
  }

}
