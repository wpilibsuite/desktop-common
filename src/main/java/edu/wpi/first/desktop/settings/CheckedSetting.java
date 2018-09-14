package edu.wpi.first.desktop.settings;

import javafx.beans.property.Property;

/**
 * A type of setting that checks the type of value when it is set; values not of type {@code T} are rejected and an
 * exception is thrown.
 *
 * @param <T> the type of the value being configured
 */
final class CheckedSetting<T> extends Setting<T> {

  private final Class<T> type;

  CheckedSetting(String name, String description, Property<T> property, Class<T> type) {
    super(name, description, property);
    this.type = type;
    property.addListener((__, old, value) -> {
      if (value != null && !type.isInstance(value)) {
        throw new ClassCastException(
            String.format(
                "Setting '%s' cannot accept values of type '%s'. Only values of type '%s' or a subclass are permitted",
                name, value.getClass().getName(), type.getName()));
      }
    });
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
