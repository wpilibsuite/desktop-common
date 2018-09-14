package edu.wpi.first.desktop.plugin;

import java.util.ArrayList;
import java.util.List;

public class MockTarget {

  private final List<Plugin<MockTarget>> appliedPlugins = new ArrayList<>();

  public void addPlugin(Plugin<MockTarget> plugin) {
    appliedPlugins.add(plugin);
  }

  public void removePlugin(Plugin<MockTarget> plugin) {
    appliedPlugins.remove(plugin);
  }

  public boolean hasPlugin(Plugin<MockTarget> plugin) {
    return appliedPlugins.contains(plugin);
  }

  public List<Plugin<MockTarget>> getAppliedPlugins() {
    return appliedPlugins;
  }
}
