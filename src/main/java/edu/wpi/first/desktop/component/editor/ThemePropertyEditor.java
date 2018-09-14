package edu.wpi.first.desktop.component.editor;

import edu.wpi.first.desktop.theme.Theme;
import edu.wpi.first.desktop.theme.ThemeContainer;

import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.AbstractPropertyEditor;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

/**
 * A property editor for {@link Theme Themes}. Themes are selectable
 */
public final class ThemePropertyEditor extends AbstractPropertyEditor<Theme, ComboBox<Theme>> {

  /**
   * Creates a new theme property editor.
   *
   * @param container the container for the themes to choose from
   * @param property  the property to edit
   */
  public ThemePropertyEditor(ThemeContainer container, PropertySheet.Item property) {
    super(property, new ComboBox<>());
    getEditor().setItems(container.getThemes());
    getEditor().setConverter(new ThemeStringConverter(container));
  }

  @Override
  protected ObservableValue<Theme> getObservableValue() {
    return getEditor().getSelectionModel().selectedItemProperty();
  }

  @Override
  public void setValue(Theme value) {
    getEditor().getSelectionModel().select(value);
  }

  private static final class ThemeStringConverter extends StringConverter<Theme> {

    private final ThemeContainer container;

    private ThemeStringConverter(ThemeContainer container) {
      this.container = container;
    }

    @Override
    public String toString(Theme theme) {
      return theme.getName();
    }

    @Override
    public Theme fromString(String string) {
      return container.getTheme(string).get();
    }
  }
}