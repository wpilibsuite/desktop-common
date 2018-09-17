package edu.wpi.first.desktop.theme;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.net.URL;
import java.util.List;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("UI")
@DisabledOnOs(OS.WINDOWS)
class ThemeTest extends ApplicationTest {

  private Stage stage;
  private final String path = "/edu/wpi/first/desktop/theme/stylesheet.css";

  @Override
  public void start(Stage stage) throws Exception {
    super.start(stage);
    this.stage = stage;
  }

  @AfterEach
  void reset() {
    runAndWait(stage::hide);
  }

  @Test
  void sanityCheck() {
    Class<NullPointerException> nullPointer = NullPointerException.class;
    assertAll("Constructors and getters sanity checks",
        () -> {
          Theme theme = new Theme("Name", "path");
          assertEquals("Name", theme.getName(), "Wrong name");
        },
        () -> {
          assertThrows(nullPointer, () -> new Theme("", (URL) null), "Null URLs should throw NPE");
        },
        () -> {
          assertThrows(nullPointer, () -> new Theme("", (String) null), "Null URLs should throw NPE");
        },
        () -> {
          assertThrows(nullPointer, () -> new Theme("", (List<URL>) null), "Null URLs should throw NPE");
        },
        () -> {
          assertThrows(nullPointer, () -> new Theme("", singletonList(null)), "Null URLs should throw NPE");
        },
        () -> {
          assertThrows(nullPointer, () -> new Theme(null, ""), "Null name should throw NPE");
        },
        () -> {
          Theme theme = new Theme("", "path");
          assertEquals(List.of("path"), theme.getStyleSheetPaths(), "Wrong paths");
        }
    );
  }

  @Test
  void testApplyStringToScene() {
    Scene scene = new Scene(new StackPane(new Button("Press me!")), 320, 240);
    Theme theme = new Theme("Test Theme", path);
    theme.applyTo(scene);
    runAndWait(() -> {
      stage.setTitle("testApplyStringToScene");
      stage.setScene(scene);
      stage.show();
    });
    assertEquals(List.of(path), scene.getStylesheets(), "Stylesheet was not applied");
  }

  @Test
  void testApplyUrlToScene() {
    Scene scene = new Scene(new StackPane(new Button("Press me!")), 320, 240);
    URL url = ThemeTest.class.getResource(path);
    Theme theme = new Theme("Test Theme", url);
    theme.applyTo(scene);
    runAndWait(() -> {
      stage.setTitle("testApplyUrlToScene");
      stage.setScene(scene);
      stage.show();
    });
    assertEquals(List.of(url.toExternalForm()), scene.getStylesheets(), "Stylesheet was not applied");
  }

  private static void runAndWait(Runnable runnable) {
    Platform.runLater(runnable);
    WaitForAsyncUtils.waitForFxEvents();
  }

}
