package edu.wpi.first.desktop.settings;

import org.controlsfx.control.PropertySheet;

import java.util.Optional;

import javafx.beans.value.ObservableValue;

/**
 * A {@link PropertySheet} item wrapping a single {@link Setting}.
 */
public final class SettingsItem implements PropertySheet.Item {
  private final Setting<?> setting;
  private final Group group;
  private final Class<?> type;

  /**
   * Creates a new settings item.
   *
   * @param group   the group containing the setting
   * @param setting the setting to wrap
   */
  public SettingsItem(Group group, Setting<?> setting) {
    this.setting = setting;
    this.group = group;
    if (setting instanceof CheckedSetting) {
      this.type = ((CheckedSetting<?>) setting).getType();
    } else {
      this.type = setting.getValue().getClass();
    }
  }

  @Override
  public Class<?> getType() {
    return type;
  }

  @Override
  public String getCategory() {
    return group.getName();
  }

  @Override
  public String getName() {
    return setting.getName();
  }

  @Override
  public String getDescription() {
    return setting.getDescription();
  }

  @Override
  public Object getValue() {
    return setting.getValue();
  }

  @Override
  @SuppressWarnings("unchecked")
  public void setValue(Object value) {
    ((Setting) setting).setValue(value);
  }

  @Override
  public Optional<ObservableValue<?>> getObservableValue() {
    return Optional.of(setting.getProperty());
  }
}
