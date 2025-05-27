package client.ui.adminView.horseList;

import client.ui.common.Controller;
import client.ui.common.ViewModel;
import client.ui.navigation.MainWindowController;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.converter.DefaultStringConverter;
import javafx.util.converter.NumberStringConverter;
import shared.DTO.HorseDTO;

/**
 * Controller for creating, editing, and removing horses in the admin view.
 *
 * Handles form field bindings, list population, and button interactions tied to horse management.
 */
public class CreateEditHorseController implements Controller {

  @FXML private ListView<HorseDTO> listView;
  @FXML private TextField horseName;
  @FXML private TextField speedMin;
  @FXML private TextField speedMax;

  @FXML private Button create;
  @FXML private Button edit;
  @FXML private Button remove;

  private CreateEditHorseVM viewModel;
  private MainWindowController mainWindowController;

  /**
   * Default constructor.
   */
  public CreateEditHorseController() {}

  /**
   * Initializes the controller by binding UI elements to the ViewModel.
   * Also sets up list rendering, selection handling, and button actions.
   *
   * @param viewModel the view model providing horse data and logic
   */
  @Override
  public void initialize(ViewModel viewModel) {
    this.viewModel = (CreateEditHorseVM) viewModel;

    // — bind the list —
    listView.setItems(this.viewModel.getHorseList());

    // — bind the form fields —
    Bindings.bindBidirectional(horseName.textProperty(), this.viewModel.horseNameProp(), new DefaultStringConverter());
    Bindings.bindBidirectional(speedMin.textProperty(), this.viewModel.speedMinProp(), new NumberStringConverter());
    Bindings.bindBidirectional(speedMax.textProperty(), this.viewModel.speedMaxProp(), new NumberStringConverter());

    // Configure cell rendering for the horse list
    listView.setCellFactory(param -> new ListCell<>() {
      @Override
      protected void updateItem(HorseDTO horse, boolean empty) {
        super.updateItem(horse, empty);
        if (empty || horse == null) {
          setText(null);
        } else {
          setText(horse.name() + " (speed: " + horse.speedMin() + " - " + horse.speedMax() + ")");
        }
      }
    });

    // Update the selected horse in the ViewModel when selection changes in the ListView
    listView.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> this.viewModel.setSelectedHorse(newVal)
    );

    // — bind button enable/disable —
    Bindings.bindBidirectional(edit.disableProperty(), this.viewModel.editButtonDisableProperty());
    Bindings.bindBidirectional(remove.disableProperty(), this.viewModel.removeButtonDisableProperty());

    // — actions —
    create.setOnAction(e -> this.viewModel.setHorseCreationMode());
    edit.setOnAction(e -> this.viewModel.updateHorse());
    remove.setOnAction(e -> this.viewModel.removeHorse());

    // — status message —
    // messageLabel.textProperty().bind(this.viewModel.messageProp());
  }

  /**
   * Sets the reference to the MainWindowController for view switching.
   *
   * @param mainWindowController the main window controller
   */
  @Override
  public void setWindowController(MainWindowController mainWindowController) {
    this.mainWindowController = mainWindowController;
  }
}
