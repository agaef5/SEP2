package client.ui.adminView.adminPanel;

import client.ui.common.Controller;
import client.ui.common.ViewModel;
import client.ui.navigation.MainWindowController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Controller for the Admin Panel view.
 *
 * Handles UI interactions for adding horses and races,
 * and binds race information from the view model to the label.
 */
public class AdminPanelController implements Controller {

  @FXML private Button addHorse;
  @FXML private Button addRace;
  @FXML private Label raceInfo;

  private AdminPanelVM viewModel;
  private MainWindowController mainWindowController;

  /**
   * Default constructor.
   */
  public AdminPanelController() {}

  /**
   * Initializes the controller with a given ViewModel.
   * Binds UI elements to properties and sets up button actions.
   *
   * @param viewModel the view model to be used by this controller
   */
  @Override
  public void initialize(ViewModel viewModel) {
    this.viewModel = (AdminPanelVM) viewModel;
    raceInfo.textProperty().bind(this.viewModel.raceInfoTextProperty());

    addHorse.setOnAction(e -> {
      mainWindowController.loadHorsePage();
    });

    addRace.setOnAction(e -> {
      mainWindowController.loadRacePage();
    });
  }

  /**
   * Sets the reference to the main window controller.
   * Allows this controller to change the active view.
   *
   * @param mainWindowController the application's main window controller
   */
  @Override
  public void setWindowController(MainWindowController mainWindowController) {
    this.mainWindowController = mainWindowController;
  }
}
