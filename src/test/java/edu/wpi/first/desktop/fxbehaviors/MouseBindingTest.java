package edu.wpi.first.desktop.fxbehaviors;

import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.event.EventType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MouseBindingTest {

  @Test
  public void test() {
    final AtomicBoolean fired = new AtomicBoolean(false);
    final MouseBinding<?> binding = MouseBinding.builder()
        .withMouseButton(MouseButton.PRIMARY)
        .onEvent(MouseEvent.MOUSE_CLICKED)
        .withAction(__ -> fired.set(true))
        .build();

    final MouseEvent withModifier = createMouseEvent(MouseEvent.MOUSE_CLICKED, MouseButton.PRIMARY, KeyCode.SHIFT);
    final MouseEvent shouldWork = createMouseEvent(MouseEvent.MOUSE_CLICKED, MouseButton.PRIMARY);

    binding.fireIfMatches(shouldWork, null);
    assertTrue(fired.get());

    fired.set(false);
    binding.fireIfMatches(withModifier, null);
    assertFalse(fired.get());
  }

  public static MouseEvent createMouseEvent(EventType<MouseEvent> eventType, MouseButton button, KeyCode... keys) {
    final Set<KeyCode> keyCodes = Set.of(keys);
    return new MouseEvent(
        null, null,
        eventType,
        0, 0,
        0, 0,
        button,
        0,
        keyCodes.contains(KeyCode.SHIFT),
        keyCodes.contains(KeyCode.CONTROL),
        keyCodes.contains(KeyCode.ALT),
        keyCodes.contains(KeyCode.META),
        button == MouseButton.PRIMARY,
        button == MouseButton.MIDDLE,
        button == MouseButton.SECONDARY,
        true,
        false,
        false,
        null
    );
  }

}