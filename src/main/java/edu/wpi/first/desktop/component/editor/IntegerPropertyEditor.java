package edu.wpi.first.desktop.component.editor;

import edu.wpi.first.desktop.component.IntegerField;

import org.controlsfx.control.PropertySheet;

import javafx.beans.value.ObservableValue;

public final class IntegerPropertyEditor extends DebouncedPropertyEditor<Integer, IntegerField> {

  public IntegerPropertyEditor(PropertySheet.Item item) {
    super(item, new IntegerField((Integer) item.getValue()));
  }

  @Override
  protected ObservableValue<Integer> getObservableValue() {
    return getEditor().numberProperty();
  }

  @Override
  public void setValue(Integer value) {
    getEditor().setNumber(value);
  }
}
