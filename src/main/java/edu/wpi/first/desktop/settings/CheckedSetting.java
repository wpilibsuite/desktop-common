package edu.wpi.first.desktop.settings;

import javafx.beans.property.Property;

/**
 * A type of setting that checks
 * @param <T>
 */
final class CheckedSetting<T> extends Setting<T> {

  private final Class<T> type;

  CheckedSetting(String name, String description, Property<T> property, Class<T> type) {
    super(name, description, property);
    this.type = type;
  }

  public Class<T> getType() {
    return type;
  }

  @Override
  public void setValue(T value) {
    if (!type.isInstance(value)) {
      throw new ClassCastException("Input value '" + value + "' is not an instance of " + type.getName());
    }
    super.setValue(value);
  }
}
