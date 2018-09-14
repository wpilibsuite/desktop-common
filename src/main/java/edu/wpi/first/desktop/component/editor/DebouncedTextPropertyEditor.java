package edu.wpi.first.desktop.component.editor;

import org.controlsfx.control.PropertySheet;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public final class DebouncedTextPropertyEditor extends DebouncedPropertyEditor<String, TextField> {

  public DebouncedTextPropertyEditor(PropertySheet.Item item) {
    super(item, new TextField((String) item.getValue()));
  }

  @Override
  protected ObservableValue<String> getObservableValue() {
    return getEditor().textProperty();
  }

  @Override
  public void setValue(String value) {
    getEditor().setText(value);
  }
}
