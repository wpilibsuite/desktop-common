package edu.wpi.first.desktop.component;

import edu.wpi.first.desktop.component.editor.IntegerPropertyEditor;
import edu.wpi.first.desktop.component.editor.NumberPropertyEditor;
import edu.wpi.first.desktop.component.editor.DebouncedTextPropertyEditor;
import edu.wpi.first.desktop.component.editor.ToggleSwitchEditor;
import edu.wpi.first.desktop.component.skin.SettingsSheetSkin;
import edu.wpi.first.desktop.settings.Category;
import edu.wpi.first.desktop.settings.Group;
import edu.wpi.first.desktop.settings.Setting;
import edu.wpi.first.desktop.settings.SettingsItem;

import org.controlsfx.control.PropertySheet;
import org.controlsfx.control.ToggleSwitch;
import org.controlsfx.property.editor.DefaultPropertyEditorFactory;
import org.controlsfx.property.editor.PropertyEditor;

import javafx.scene.control.Skin;
import javafx.util.Callback;

/**
 * A version of {@link PropertySheet} that has better support for editing numbers (using {@link NumberField} and
 * {@link IntegerField} for doubles and integers, respectively) and booleans (using {@link ToggleSwitch}).
 * If additional editors are required for application-specific types,
 * {@link #setPropertyEditorFactory set the property editor factory} that performs checking for the type and does
 * fallthrough to {@link #DEFAULT_EDITOR_FACTORY the default editor factory}. For example, adding an editor to select
 * {@link edu.wpi.first.desktop.theme.Theme Themes}:
 * <pre>{@code
 * ThemeContainer myThemeContainer = ...
 * SettingsSheet settingsSheet = ...
 * settingsSheet.setPropertyEditorFactory(item -> {
 *   if (item.getType() == Theme.class) {
 *     return new ThemePropertyEditor(myThemeContainer, item);
 *   } else {
 *     return SettingsSheet.DEFAULT_EDITOR_FACTORY.call(item);
 *   }
 * });
 * }</pre>
 */
public final class SettingsSheet extends PropertySheet {

  /**
   * The default factory for settings editors. This handles text editors, integers, floating-point numbers, and
   * booleans.
   */
  public static final Callback<Item, PropertyEditor<?>> DEFAULT_EDITOR_FACTORY = new DefaultPropertyEditorFactory() {
    @Override
    public PropertyEditor<?> call(Item item) {
      if (item.getType() == String.class) {
        return new DebouncedTextPropertyEditor(item);
      }
      if (isIntegerType(item)) {
        return new IntegerPropertyEditor(item);
      }
      if (isNumericType(item)) {
        return new NumberPropertyEditor(item);
      }
      if (item.getType() == Boolean.class) {
        return new ToggleSwitchEditor(item);
      }
      return super.call(item);
    }

    private boolean isIntegerType(Item item) {
      return item.getType() == Integer.class
          || item.getType() == Long.class
          || item.getType() == Byte.class
          || item.getType() == Short.class;
    }

    private boolean isNumericType(Item item) {
      return Number.class.isAssignableFrom(item.getType());
    }
  };

  /**
   * Creates an empty property sheet.
   */
  public SettingsSheet() {
    super();
    setModeSwitcherVisible(false);
    setSearchBoxVisible(false);
    setPropertyEditorFactory(DEFAULT_EDITOR_FACTORY);
  }

  /**
   * Creates a property sheet for editing the settings in a category. This does <i>not</i> include settings for
   * subcategories.
   */
  public SettingsSheet(Category settingsCategory) {
    this();
    addCategory(settingsCategory);
  }

  public void addCategory(Category category) {
    setMode(PropertySheet.Mode.CATEGORY);
    for (Group group : category.getGroups()) {
      for (Setting<?> setting : group.getSettings()) {
        PropertySheet.Item item = new SettingsItem(group, setting);
        getItems().add(item);
      }
    }
  }

  @Override
  protected Skin<?> createDefaultSkin() {
    return new SettingsSheetSkin(this);
  }
}
