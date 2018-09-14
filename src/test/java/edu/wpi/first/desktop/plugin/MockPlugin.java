package edu.wpi.first.desktop.plugin;

import java.util.Collection;
import java.util.Set;

public class MockPlugin implements Plugin<MockTarget> {

  private final Descriptor descriptor;
  private final Collection<Requirement> requirements;

  public MockPlugin(Descriptor descriptor, Collection<Requirement> requirements) {
    this.descriptor = descriptor;
    this.requirements = requirements;
  }

  public MockPlugin(Descriptor descriptor, Requirement... requirements) {
    this(descriptor, Set.of(requirements));
  }

  @Override
  public void applyTo(MockTarget target) {
    target.addPlugin(this);
  }

  @Override
  public void removeFrom(MockTarget target) {
    target.removePlugin(this);
  }

  @Override
  public Descriptor descriptor() {
    return descriptor;
  }

  @Override
  public Collection<Requirement> requirements() {
    return requirements;
  }
}
