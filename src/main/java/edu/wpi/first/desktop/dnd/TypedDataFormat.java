package edu.wpi.first.desktop.dnd;

import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;

/**
 * @param <T> the expected type of data corresponding to this data format
 */
public class TypedDataFormat<T> extends DataFormat {

  public TypedDataFormat(String... ids) {
    super(ids);
  }

  /**
   * Gets data of this format from a clipboard.
   *
   * @param clipboard the clipboard to get data from
   *
   * @return the data for the given data format, or {@code null} if no such data is present in the clipboard
   *
   * @throws ClassCastException if the content in the clipboard for the data format is not of type {@code T}
   */
  public T get(Clipboard clipboard) {
    return (T) clipboard.getContent(this);
  }
}
