/**
 * Provides various utility classes for use by the WPILib desktop applications that use JavaFX.
 */
module edu.wpi.first.desktop {
  requires javafx.graphics;
  requires javafx.controls;

  exports edu.wpi.first.desktop.dnd;
  exports edu.wpi.first.desktop.plugin;
  exports edu.wpi.first.desktop.theme;
  exports edu.wpi.first.desktop.util;
}
