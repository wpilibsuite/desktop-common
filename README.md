# WPILib Desktop Commons

This is a library containing code shared by the various WPILib desktop applications.

**WPILib JavaFX Applications**  
- [Shuffleboard](https://github.com/wpilibsuite/shuffleboard)
- [GRIP](https://github.com/wpiroboticsprojects/grip)
- [OutlineViewer](https://github.com/wpilibsuite/outlineviewer)
- [PathWeaver](https://github.com/wpilibsuite/pathweaver)

## Packages

**edu.wpi.first.desktop.component**  
Useful controls and UI components.

**edu.wpi.first.desktop.dnd**  
Common classes for dealing with drag-and-drop and data formats.

**edu.wpi.first.desktop.plugin**  
Common classes for application plugins.

**edu.wpi.first.desktop.property**  
Custom JavaFX property implementations.

**edu.wpi.first.desktop.settings**  
A settings API and dialog for a shared experience across the desktop apps.

**edu.wpi.first.desktop.theme**  
Common classes for styling applications with CSS stylesheets.

**edu.wpi.first.desktop.util**  
Miscellaneous classes for dealing with concurrency on the JavaFX application thread, system properties, and more.

## Module Info
This library uses the module name `edu.wpi.first.desktop` and exports the packages listed above.

## Building

### Requirements
desktop-commons is designed to be built on Java 11 due to the JavaFX split in that release.

Use `./gradlew jar` to generate a standalone JAR file
