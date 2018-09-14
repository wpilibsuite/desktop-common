package edu.wpi.first.desktop.theme;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.WeakHashMap;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * Manages themes on various scenes and nodes. Setting the theme of a theme manager will immediately apply the theme to
 * all scenes and nodes that have been added to the manager with {@link #addScene} and {@link #addNode}.
 * Adding scenes and nodes after a theme has been set will immediately apply that theme.
 *
 * <p>The default theme is {@link Theme#MODENA Modena}.</p>
 */
public final class ThemeManager {

  // Use weak references to let the scenes and nodes get garbage collected
  private final Set<Scene> scenes = Collections.newSetFromMap(new WeakHashMap<>());
  private final Set<Parent> nodes = Collections.newSetFromMap(new WeakHashMap<>());

  private final ObjectProperty<Theme> theme = new SimpleObjectProperty<>(this, "theme", Theme.MODENA);

  /**
   * Creates a new theme manager that by default has nothing to manage. Scenes and nodes can be managed by calling
   * {@link #addScene(Scene)} and {@link #addNode(Parent)} on newly created managers.
   */
  public ThemeManager() {
    theme.addListener((__, old, theme) -> {
      for (Scene scene : scenes) {
        applyThemeOrDefault(theme, scene);
      }
      for (Parent node : nodes) {
        applyThemeOrDefault(theme, node);
      }
    });
  }

  /**
   * Adds a scene to this manager. The current theme will immediately be applied to the scene.
   *
   * @param scene the scene to manage the theme of
   */
  public void addScene(Scene scene) {
    scenes.add(scene);
    applyThemeOrDefault(getTheme(), scene);
  }

  /**
   * Removes a scene from this manager and resets its stylesheets to the JavaFX default.
   *
   * @param scene the scene to unmanage
   */
  public void removeScene(Scene scene) {
    scenes.remove(scene);
    Theme.MODENA.applyTo(scene);
  }

  /**
   * Adds a node to this manager. The current theme will immediately be applied to the node.
   *
   * @param node the node to manage the theme of
   */
  public void addNode(Parent node) {
    nodes.add(node);
    applyThemeOrDefault(getTheme(), node);
  }

  /**
   * Removes a node from this manager and resets its stylesheets to the JavaFX default.
   *
   * @param node the node to unmanage
   */
  public void removeNode(Parent node) {
    nodes.remove(node);
    Theme.MODENA.applyTo(node);
  }

  /**
   * Gets the theme used by this manager and all managed scenes and nodes.
   *
   * @return the current theme
   */
  public Theme getTheme() {
    return theme.get();
  }

  public ObjectProperty<Theme> themeProperty() {
    return theme;
  }

  /**
   * Sets the theme used by this manager and all managed scenes and nodes.
   *
   * @param theme the new theme to use
   */
  public void setTheme(Theme theme) {
    this.theme.set(theme);
  }

  private static void applyThemeOrDefault(Theme theme, Scene scene) {
    Objects.requireNonNullElse(theme, Theme.MODENA).applyTo(scene);
  }

  private static void applyThemeOrDefault(Theme theme, Parent node) {
    Objects.requireNonNullElse(theme, Theme.MODENA).applyTo(node);
  }
}
