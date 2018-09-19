/**
 * Provides various utility classes for use by the WPILib desktop applications that use JavaFX.
 */
module edu.wpi.first.desktop {
  requires java.desktop;
  requires javafx.graphics;
  requires javafx.controls;

  exports edu.wpi.first.desktop.component; // note: the skin package is not exported
  exports edu.wpi.first.desktop.component.editor;
  exports edu.wpi.first.desktop.dnd;
  exports edu.wpi.first.desktop.fxbehaviors;
  exports edu.wpi.first.desktop.plugin;
  exports edu.wpi.first.desktop.property;
  exports edu.wpi.first.desktop.settings;
  exports edu.wpi.first.desktop.theme;
  exports edu.wpi.first.desktop.util;

  exports org.controlsfx.control;
  exports org.controlsfx.control.action;
  exports org.controlsfx.dialog;
  exports org.controlsfx.glyphfont;
  exports org.controlsfx.property;
  exports org.controlsfx.property.editor;
  exports org.controlsfx.tools;

  uses org.controlsfx.glyphfont.GlyphFont;
}
