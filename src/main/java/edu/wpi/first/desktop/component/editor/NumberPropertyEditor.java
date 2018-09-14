package edu.wpi.first.desktop.component.editor;

import edu.wpi.first.desktop.component.NumberField;

import org.controlsfx.control.PropertySheet;

import javafx.beans.value.ObservableValue;

/**
 * A property editor for numbers. We use this instead of the one bundled with ControlsFX because
 * their implementation is bad.
 */
public final class NumberPropertyEditor extends DebouncedPropertyEditor<Double, NumberField> {

  public NumberPropertyEditor(PropertySheet.Item item) {
    super(item, new NumberField(((Number) item.getValue()).doubleValue()));
  }

  @Override
  protected ObservableValue<Double> getObservableValue() {
    return getEditor().numberProperty();
  }

  @Override
  public void setValue(Double value) {
    getEditor().setNumber(value);
  }
}
