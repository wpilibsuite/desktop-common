package edu.wpi.first.desktop.component.editor;

import edu.wpi.first.desktop.property.FlushableProperty;
import edu.wpi.first.desktop.util.Debouncer;
import edu.wpi.first.desktop.util.FxUtils;
import edu.wpi.first.desktop.util.TypeUtils;

import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.AbstractPropertyEditor;

import java.time.Duration;

import javafx.scene.control.Control;

/**
 * A property editor that will debounce user inputs (for example, when typing in text) for an item backed by
 * a {@link FlushableProperty}.
 *
 * @param <T> the type of the data in the editor
 * @param <C> the type of the node to use to edit the property
 */
public abstract class DebouncedPropertyEditor<T, C extends Control> extends AbstractPropertyEditor<T, C> {

  /**
   * The default debounce delay of 250ms.
   */
  public static final Duration DEFAULT_DEBOUNCE_DELAY = Duration.ofMillis(250);

  protected DebouncedPropertyEditor(PropertySheet.Item property, C control, Duration debounceDelay) {
    super(property, control);
    property.getObservableValue()
        .flatMap(TypeUtils.optionalCast(FlushableProperty.class))
        .ifPresent(flushable -> {
          Debouncer debouncer = new Debouncer(() -> FxUtils.runOnFxThread(flushable::flush), debounceDelay);
          getObservableValue().addListener((__, oldValue, newValue) -> debouncer.run());
        });
  }

  protected DebouncedPropertyEditor(PropertySheet.Item property, C control) {
    this(property, control, DEFAULT_DEBOUNCE_DELAY);
  }
}
