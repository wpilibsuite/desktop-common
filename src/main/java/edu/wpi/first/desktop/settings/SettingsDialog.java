package edu.wpi.first.desktop.settings;

import edu.wpi.first.desktop.component.SettingsSheet;
import edu.wpi.first.desktop.property.FlushableProperty;
import edu.wpi.first.desktop.util.TypeUtils;

import java.util.List;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.StackPane;

public final class SettingsDialog {

  private final TreeItem<Category> rootItem = new TreeItem<>();
  private final TreeView<Category> categories = new TreeView<>(rootItem);
  private final StackPane view = new StackPane();
  private final SplitPane root = new SplitPane(categories, view);

  private final SettingsSheet settingsSheet = new SettingsSheet();

  public SettingsDialog() {
    initialize();
  }

  private void initialize() {
    rootItem.setExpanded(true);
    categories.setMinWidth(180);
    categories.getStyleClass().add("settings-categories");
    categories.setShowRoot(false);

    categories.setCellFactory(v -> {
      TreeCell<Category> cell = new TreeCell<>();
      cell.setPrefWidth(1);
      cell.textProperty().bind(Bindings.createObjectBinding(() -> cell.getItem().getName(), cell.itemProperty()));
      return cell;
    });
    categories.getSelectionModel().selectedItemProperty().addListener((__, old, item) -> {
      setViewForCategory(item.getValue());
    });

    rootItem.getChildren().addListener((InvalidationListener) __ -> {
      if (!rootItem.getChildren().isEmpty()) {
        categories.getSelectionModel().select(0);
        setViewForCategory(rootItem.getChildren().get(0).getValue());
      }
    });

    Platform.runLater(() -> root.setDividerPositions(0));
  }

  private void setViewForCategory(Category category) {
    if (category.getGroups().isEmpty()) {
      view.getChildren().setAll(new Label("No settings for " + category.getName()));
    } else {
      settingsSheet.getItems().clear();
      settingsSheet.addCategory(category);
    }
  }

  /**
   * Sets the root settings categories to display in the settings view. Subcategories will be displayed as subtrees
   * underneath their parent category.
   *
   * @param rootCategories the root categories to display in the settings view
   */
  public void setRootCategories(List<Category> rootCategories) {
    rootItem.getChildren().clear();
    for (Category rootCategory : rootCategories) {
      TreeItem<Category> item = new TreeItem<>(rootCategory);
      addSubcategories(item);
      rootItem.getChildren().add(item);
    }
  }

  private void addSubcategories(TreeItem<Category> rootItem) {
    rootItem.getValue().getSubcategories()
        .forEach(category -> {
          rootItem.setExpanded(true);
          TreeItem<Category> item = new TreeItem<>(category);
          addSubcategories(item);
          rootItem.getChildren().add(item);
        });
  }

  /**
   * Applies the user-made changes to the settings. Most editors update their respective properties immediately,
   * but those backed by flushable properties need to be manually updated using this method.
   */
  public void applySettings() {
    applySettings(rootItem);
  }

  private void applySettings(TreeItem<Category> item) {
    if (item.getValue() != null) {
      item.getValue().getGroups()
          .stream()
          .map(Group::getSettings)
          .flatMap(List::stream)
          .map(Setting::getProperty)
          .flatMap(TypeUtils.castStream(FlushableProperty.class))
          .filter(FlushableProperty::isChanged)
          .forEach(FlushableProperty::flush);
    }
    item.getChildren().forEach(this::applySettings);
  }
}
