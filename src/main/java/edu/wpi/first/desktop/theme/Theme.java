package edu.wpi.first.desktop.theme;

import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.scene.Parent;
import javafx.scene.Scene;

public final class Theme {

  /**
   * Modena is the default theme for JavaFX applications; applications using this theme have the default stylesheets
   * applied.
   */
  public static final Theme MODENA = new Theme("Modena", new String[0]);

  // Cache these to avoid creating unnecessary objects when instantiating Theme objects
  private static final Consumer<URL> validateUrl = Theme::validateUrl;
  private static final Function<URL, String> toExternalForm = URL::toExternalForm;
  private static final Collector<String, ?, List<String>> stringListCollector = Collectors.toList();

  private final String name;
  private final List<String> styleSheetPaths;

  /**
   * Creates a theme with stylesheets relative to some class. This should be used when stylesheets are in JAR files
   * not on the application boot classpath (i.e. not loaded by the boot classloader).
   *
   * @param name      the name of the theme
   * @param reference a class in the JAR file to use as a reference
   * @param paths     the paths to the stylesheets, relative to the reference class
   *
   * @return a new theme object
   */
  public static Theme relativeToClass(String name, Class<?> reference, String... paths) {
    return new Theme(name, Stream.of(paths).map(reference::getResource).collect(Collectors.toList()));
  }

  /**
   * Creates a theme with stylesheets relative to some class. This should be used when stylesheets are in JAR files
   * not on the application boot classpath (i.e. not loaded by the boot classloader).
   *
   * @param name      the name of the theme
   * @param reference a class in the JAR file to use as a reference
   * @param paths     the paths to the stylesheets, relative to the reference class
   *
   * @return a new theme object
   */
  public static Theme relativeToClass(String name, Class<?> reference, Collection<String> paths) {
    return new Theme(name, paths.stream().map(reference::getResource).collect(Collectors.toList()));
  }

  /**
   * Creates a new theme.
   *
   * @param name            the name of the theme
   * @param styleSheetPaths the paths to the stylesheets used by this theme
   *
   * @throws NullPointerException if given a null stylesheet path
   */
  public Theme(String name, Collection<URL> styleSheetPaths) {
    this.name = Objects.requireNonNull(name, "name");
    this.styleSheetPaths = styleSheetPaths.stream()
        .peek(validateUrl)
        .map(toExternalForm)
        .collect(stringListCollector);
  }

  /**
   * Creates a new theme.
   *
   * @param name            the name of the theme
   * @param styleSheetPaths the paths to the stylesheets used by this theme
   *
   * @throws NullPointerException if given a null stylesheet path
   */
  public Theme(String name, URL... styleSheetPaths) {
    this.name = Objects.requireNonNull(name, "name");
    this.styleSheetPaths = Arrays.stream(styleSheetPaths)
        .peek(validateUrl)
        .map(toExternalForm)
        .collect(stringListCollector);
  }

  /**
   * Creates a new theme.
   *
   * @param name            the name of the theme
   * @param styleSheetPaths the paths to the stylesheets used by this theme
   *
   * @throws NullPointerException if given a null stylesheet path
   * @see Scene#getStylesheets()
   */
  public Theme(String name, String... styleSheetPaths) {
    this.name = Objects.requireNonNull(name, "name");
    this.styleSheetPaths = List.of(styleSheetPaths); // Handles null checks internally
  }

  private static void validateUrl(URL url) {
    Objects.requireNonNull(url, "Null URLs are not allowed");
  }

  /**
   * Gets the name of this theme.
   *
   * @return the name of this theme
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the paths to the stylesheets used by this theme. The returned list is read-only.
   *
   * @return a read-only list of the stylesheet paths
   *
   * @see Scene#getStylesheets()
   */
  public List<String> getStyleSheetPaths() {
    return styleSheetPaths;
  }

  /**
   * Applies this theme to a scene. Any stylesheets present on the scene (including Caspian and Modena) will be removed.
   *
   * @param scene the scene to which to apply this theme
   */
  public void applyTo(Scene scene) {
    scene.getStylesheets().setAll(styleSheetPaths);
  }

  /**
   * Applies this theme to a parent node. Any stylesheets already present on the node will be removed.
   *
   * @param parent the node to which to apply this theme
   */
  public void applyTo(Parent parent) {
    parent.getStylesheets().setAll(styleSheetPaths);
  }
}
