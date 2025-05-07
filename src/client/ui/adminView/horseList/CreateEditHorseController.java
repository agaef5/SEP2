package client.ui.adminView.horseList;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.converter.BooleanStringConverter;
import javafx.util.converter.DefaultStringConverter;
import javafx.util.converter.NumberStringConverter;
import server.model.Horse;

/**
 * Controller for the Create/Edit Horse view.
 * Manages user interactions for creating, editing, and removing horse entries.
 * Connects UI components to the corresponding ViewModel.
 */
public class CreateEditHorseController
{
  /** ListView displaying all available horses */
  @FXML private ListView<Horse> listView;

  /** Text field for editing horse name */
  @FXML private TextField horseName;

  /** Text field for editing minimum speed value */
  @FXML private TextField speedMin;

  /** Text field for editing maximum speed value */
  @FXML private TextField speedMax;

  /** Button to create a new horse */
  @FXML private Button create;

  /** Button to edit the selected horse */
  @FXML private Button edit;

  /** Button to remove the selected horse */
  @FXML private Button remove;

  /** ViewModel that provides data and operations for this view */
  private CreateEditHorseVM viewModel;

  /**
   * Initializes the controller with the provided ViewModel.
   * Sets up bindings between UI components and ViewModel properties,
   * configures cell rendering for the horse list, and attaches event handlers.
   *
   * @param viewModel The ViewModel that provides data and operations for this view
   */
  public void init(CreateEditHorseVM viewModel) {
    this.viewModel = viewModel;

    // Bind the ListView to the horse list in the ViewModel
    listView.setItems(viewModel.getHorseList());

    // Configure cell rendering for the horse list
    listView.setCellFactory(param -> new ListCell<>() {
      @Override
      protected void updateItem(Horse horse, boolean empty) {
        super.updateItem(horse, empty);
        if (empty || horse == null) {
          setText(null);
        } else {
          setText(horse.getName() + " (speed: " + horse.getSpeedMin() + " - " + horse.getSpeedMax() + ")");
        }
      }
    });

    // Update the selected horse in the ViewModel when selection changes in the ListView
    listView.getSelectionModel().selectedItemProperty().addListener(
        (obs, oldVal, newVal) -> viewModel.setSelectedHorse(newVal)
    );

    // Bind text fields to ViewModel properties with appropriate converters
    Bindings.bindBidirectional(horseName.textProperty(), viewModel.horseNameProperty(), new DefaultStringConverter());
    Bindings.bindBidirectional(speedMin.textProperty(), viewModel.speedMinProperty(), new NumberStringConverter());
    Bindings.bindBidirectional(speedMax.textProperty(), viewModel.speedMaxProperty(), new NumberStringConverter());

    // Bind button disabled states to ViewModel properties
    Bindings.bindBidirectional(edit.disableProperty(), viewModel.getEditButtonDisabledProperty());
    Bindings.bindBidirectional(remove.disableProperty(), viewModel.getRemoveButtonDisableProperty());

    // Configure button actions
    create.setOnAction(e -> viewModel.setHorseCreationMode());
    edit.setOnAction(e -> viewModel.updateHorse());
    remove.setOnAction(e -> viewModel.removeHorse());
  }
}