package edu.wpi.first.desktop.fxbehaviors;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.event.EventType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InputBindingsTest {

  @Test
  public void testWithNonPassingFilter() {
    final AtomicBoolean fired = new AtomicBoolean(false);
    final EventType<KeyEvent> eventType = KeyEvent.KEY_PRESSED;
    final KeyCode keyCode = KeyCode.A;
    final InputBindings<?> bindings = InputBindings.of((e, b) -> false, List.of(
        KeyBinding.builder()
            .onEvent(eventType)
            .withKey(keyCode)
            .withAction(__ -> fired.set(true))
            .build()
    ));

    bindings.fire(KeyBindingTest.createKeyEvent(eventType, keyCode), null);
    assertFalse(fired.get(), "Bindings should not fire when the filter cannot be passed");
  }

  @Test
  public void testCombinedBindings() {
    final AtomicBoolean firstFired = new AtomicBoolean(false);
    final AtomicBoolean secondFired = new AtomicBoolean(false);
    final EventType<KeyEvent> eventType = KeyEvent.KEY_PRESSED;
    final KeyCode firstKeyCode = KeyCode.A;
    final KeyCode secondKeyCode = KeyCode.B;
    InputBindings first = InputBindings.of(
        KeyBinding.builder()
            .onEvent(eventType)
            .withKey(firstKeyCode)
            .withAction(__ -> firstFired.set(true))
            .build());
    InputBindings second = InputBindings.of(
        KeyBinding.builder()
            .onEvent(eventType)
            .withKey(secondKeyCode)
            .withAction(__ -> secondFired.set(true))
            .build());
    InputBindings<?> combine = InputBindings.combine(first, second);
    combine.fire(KeyBindingTest.createKeyEvent(eventType, firstKeyCode), null);
    assertAll(
        () -> assertTrue(firstFired.get(), "First bindings should have fired"),
        () -> assertFalse(secondFired.get(), "Second bindings should not have fired")
    );
    firstFired.set(false);
    combine.fire(KeyBindingTest.createKeyEvent(eventType, secondKeyCode), null);
    assertAll(
        () -> assertFalse(firstFired.get(), "First bindings should not have fired"),
        () -> assertTrue(secondFired.get(), "Second bindings should have fired")
    );
  }

}
