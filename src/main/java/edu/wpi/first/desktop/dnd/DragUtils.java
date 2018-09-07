package edu.wpi.first.desktop.dnd;

import edu.wpi.first.desktop.util.UtilityClass;

import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;

/**
 * Utility class for dealing with various drag and drop operations.
 */
public final class DragUtils extends UtilityClass {

  /**
   * Gets data from a dragboard for the given format, casting it as necessary.
   *
   * @param dragboard the dragboard to get data from
   * @param format    the data format to get the data for
   * @param <T>       the expected type of the data
   *
   * @return the data for the given data format, or {@code null} if no such data is present in the dragboard
   *
   * @throws ClassCastException if the content in the dragboard for the data format is not of type {@code T}
   */
  public static <T> T getData(Dragboard dragboard, DataFormat format) {
    return (T) dragboard.getContent(format);
  }
}
