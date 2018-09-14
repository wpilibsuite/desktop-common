package edu.wpi.first.desktop.plugin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class PluginManagerTest {

  private MockTarget target;
  private PluginManager<MockTarget> pluginManager;

  private static final Descriptor BASE_DESCRIPTOR = new Descriptor("group", "name", Version.of(1, 0, 0), "summary");
  private static final Requirement BASE_REQUIREMENT = Requirement.from(BASE_DESCRIPTOR);
  private static final MockPlugin BASE_PLUGIN = new MockPlugin(BASE_DESCRIPTOR);

  @BeforeEach
  void setup() {
    target = new MockTarget();
    pluginManager = new PluginManager<>(target);
  }

  @Test
  void testLoadPluginWithNoRequirements() {
    MockPlugin plugin = BASE_PLUGIN;
    assertTrue(pluginManager.canLoad(plugin), "A plugin with no requirements should always be loadable");
    pluginManager.load(plugin);
    assertEquals(Set.of(plugin), pluginManager.getKnownPlugins(), "Plugin was not added to known plugins");
    assertEquals(Set.of(plugin), pluginManager.getLoadedPlugins(), "Plugin was not loaded");
    assertTrue(pluginManager.isLoaded(plugin), "Plugin was not loaded");
    assertTrue(target.hasPlugin(plugin), "Plugin was not applied to the target");
  }

  @Test
  void testLoadPluginWithLoadedRequirements() {
    MockPlugin plugin = new MockPlugin(new Descriptor("group", "dep", Version.of(1, 1, 1), ""), BASE_REQUIREMENT);
    assertFalse(pluginManager.canLoad(plugin), "A plugin without loaded requirements should not be loadable");
    pluginManager.load(BASE_PLUGIN);
    pluginManager.load(plugin);
    assertEquals(Set.of(plugin, BASE_PLUGIN), pluginManager.getKnownPlugins(), "Plugin was not added to known plugins");
    assertEquals(Set.of(plugin, BASE_PLUGIN), pluginManager.getLoadedPlugins(), "Plugin was not loaded");
    assertTrue(pluginManager.isLoaded(plugin), "Plugin was not loaded");
    assertTrue(target.hasPlugin(plugin), "Plugin was not applied to the target");
  }

  @Test
  void testLoadPluginWithoutLoadedRequirements() {
    MockPlugin plugin = new MockPlugin(new Descriptor("group", "dep", Version.of(1, 1, 1), ""), BASE_REQUIREMENT);
    assertFalse(pluginManager.canLoad(plugin), "A plugin without loaded requirements should not be loadable");
    pluginManager.load(plugin);
    assertEquals(Set.of(plugin), pluginManager.getKnownPlugins(), "Plugin was not added to known plugins");
    assertEquals(Set.of(), pluginManager.getLoadedPlugins(), "Plugin should not have been loaded");
    assertFalse(pluginManager.isLoaded(plugin), "Plugin should not have been loaded");
    assertEmpty(target.getAppliedPlugins());
  }

  @Test
  void testUnloadPluginWithNoDependents() {
    pluginManager.load(BASE_PLUGIN);
    pluginManager.unload(BASE_PLUGIN);
    assertEquals(Set.of(BASE_PLUGIN), pluginManager.getKnownPlugins());
    assertEquals(Set.of(), pluginManager.getLoadedPlugins());
    assertFalse(pluginManager.isLoaded(BASE_PLUGIN));
    assertEmpty(target.getAppliedPlugins());
  }

  @Test
  void testUnloadPluginWithSingleDirectDependent() {
    pluginManager.load(BASE_PLUGIN);
    MockPlugin dependent = new MockPlugin(new Descriptor("g", "n", Version.of(1, 0, 0), ""), BASE_REQUIREMENT);
    pluginManager.load(dependent);
    pluginManager.unload(BASE_PLUGIN);
    assertEquals(Set.of(dependent, BASE_PLUGIN), pluginManager.getKnownPlugins());
    assertEmpty(pluginManager.getLoadedPlugins());
    assertFalse(pluginManager.isLoaded(BASE_PLUGIN));
    assertFalse(pluginManager.isLoaded(dependent));
    assertEmpty(target.getAppliedPlugins());
  }

  @Test
  void testUnloadWithTransitiveDownstreamDependencies() {
    pluginManager.load(BASE_PLUGIN);
    MockPlugin direct = new MockPlugin(
        new Descriptor("g", "d", Version.of(1, 0, 0), ""),
        BASE_REQUIREMENT);
    MockPlugin transitive = new MockPlugin(
        new Descriptor("g", "t", Version.of(1, 0, 0), ""),
        Requirement.from(direct.descriptor()));
    pluginManager.load(direct);
    pluginManager.load(transitive);
    assertEquals(Set.of(BASE_PLUGIN, direct, transitive), pluginManager.getLoadedPlugins());
    pluginManager.unload(BASE_PLUGIN);
    assertEmpty(pluginManager.getLoadedPlugins());
    assertEmpty(target.getAppliedPlugins());
  }

  private static void assertEmpty(Collection<?> collection) {
    if (!collection.isEmpty()) {
      fail("Collection should be empty, but was " + collection);
    }
  }
}
