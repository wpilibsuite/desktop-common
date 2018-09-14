package edu.wpi.first.desktop.component.editor;

import org.controlsfx.control.PropertySheet;
import org.controlsfx.control.ToggleSwitch;
import org.controlsfx.property.editor.AbstractPropertyEditor;

import javafx.beans.value.ObservableValue;

public final class ToggleSwitchEditor extends AbstractPropertyEditor<Boolean, ToggleSwitch> {

  public ToggleSwitchEditor(PropertySheet.Item item) {
    super(item, new ToggleSwitch());
  }

  @Override
  protected ObservableValue<Boolean> getObservableValue() {
    return getEditor().selectedProperty();
  }

  @Override
  public void setValue(Boolean value) {
    getEditor().setSelected(value);
  }
}
