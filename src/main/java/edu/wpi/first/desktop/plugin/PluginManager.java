package edu.wpi.first.desktop.plugin;

import java.util.LinkedHashSet;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

/**
 * Manages loading and unloading plugins from a single, shared target object.
 *
 * @param <T> the type of the target managed plugins apply to
 */
public class PluginManager<T> {

  private final T target;
  private final ObservableSet<Plugin<T>> knownPlugins = FXCollections.observableSet(new LinkedHashSet<>());
  private final ObservableSet<Plugin<T>> loadedPlugins = FXCollections.observableSet(new LinkedHashSet<>());

  /**
   * Creates a new plugin manager for the given target object.
   */
  public PluginManager(T target) {
    this.target = target;
  }

  /**
   * Loads a plugin and applies it to the target. Has no effect if the plugin cannot be loaded.
   *
   * @param plugin the plugin to load
   *
   * @see #canLoad(Plugin)
   * @see #unload(Plugin)
   */
  public void load(Plugin<T> plugin) {
    if (canLoad(plugin)) {
      plugin.applyTo(target);
      loadedPlugins.add(plugin);
    }
    knownPlugins.add(plugin);
  }

  /**
   * Checks if a plugin is currently loaded.
   *
   * @param plugin the plugin to check
   *
   * @return true if the plugin is loaded, false if it is not loaded
   */
  public boolean isLoaded(Plugin<T> plugin) {
    return loadedPlugins.contains(plugin);
  }

  /**
   * Unloads a plugin and removes it from the target. Any loaded plugins that depend on it will also be unloaded.
   *
   * @param plugin the plugin to unload
   */
  public void unload(Plugin<T> plugin) {
    plugin.removeFrom(target);
    unloadDependents(plugin);
    loadedPlugins.remove(plugin);
  }

  private void unloadDependents(Plugin<T> dependency) {
    loadedPlugins.stream()
        .filter(p -> p.dependsOn(dependency))
        .collect(Collectors.toList())
        .forEach(this::unload);
  }

  /**
   * Checks if a plugin can be loaded; that is, it meets all of the following conditions:
   * <ul>
   * <li>All of its requirements are currently loaded; and</li>
   * <li>No known plugin shares its name and group ID</li>
   * </ul>
   *
   * @param plugin the plugin to check
   *
   * @return true if the plugin can be loaded, false if not
   */
  public boolean canLoad(Plugin<T> plugin) {
    return knownPlugins.stream()
        .noneMatch(p -> p.descriptor().idString().equals(plugin.descriptor().idString()))
        && plugin.requirements().stream()
        .allMatch(r -> loadedPlugins.stream().anyMatch(p -> r.matches(p.descriptor())));
  }

  /**
   * Gets the set of plugins that have ever been attempted to be loaded.
   */
  public ObservableSet<Plugin<T>> getKnownPlugins() {
    return knownPlugins;
  }

  /**
   * Gets the set of currently loaded plugins.
   */
  public ObservableSet<Plugin<T>> getLoadedPlugins() {
    return loadedPlugins;
  }
}
