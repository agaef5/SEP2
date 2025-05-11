package client.ui.adminView.adminPanel;

import client.ui.common.Controller;
import client.ui.common.ViewModel;
import client.ui.navigation.MainWindowController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

/**
 * Controller class for the admin panel view.
 * Handles user interactions and manages the display of upcoming race information.
 */
public class AdminPanelController implements Controller
{
  /** Button to navigate to the Add Horse screen */
  @FXML private Button addHorse;

  /** Button to navigate to the Add Race screen */
  @FXML private Button addRace;

  /** Button to navigate to the Edit User screen */
  @FXML private Button editUser;

  /** Label to display information about the next upcoming race */
  @FXML private Label raceInfo;

  /** ViewModel that provides data and business logic for this view */
  private AdminPanelVM viewModel;

  /** Controller that allows to control changing the view inside the main window*/
  private MainWindowController mainWindowController;

  /**
   * Default empty constructor required by FXML loader.
   */
  public AdminPanelController(){};

  /**
   * Initializes the controller with the provided ViewModel.
   * Sets up bindings between UI components and ViewModel properties,
   * and configures action handlers for buttons.
   *
   * @param viewModel The ViewModel that provides data and business logic for this view
   */
  @FXML
  public void initialize(ViewModel viewModel)
  {
    this.viewModel = (AdminPanelVM) viewModel;

    // Bind the race info label to the ViewModel property
    raceInfo.textProperty().bind(this.viewModel.getNextRaceInfo());

//    // Set up navigation actions for buttons
//    addRace.setOnAction(e -> loadScene(
//        "/client/ui/adminView/race/CreateRace.fxml"));
//    addHorse.setOnAction(e -> loadScene("/client/ui/racerList/adminView/racer/CreateEditRacer.fxml"));
//    //    editUser.setOnAction(e -> loadScene("/client/ui/racerList/adminView/user/EditUser.fxml"));
  }
  @Override
  public void setWindowController(MainWindowController mainWindowController) {
    if(mainWindowController != null)
      this.mainWindowController = mainWindowController;
  }

  /**
   * Loads a new scene and sets it as the current scene in the application window, using the Window controller.
   *
   * @param event - "event" triggered by clicking on one of the buttons on the AdminController
   */
  public void loadPage(ActionEvent event){
      if (event.getSource() == addHorse) {
        mainWindowController.loadHorsePage();
      }

      if (event.getSource() == addRace) {
        mainWindowController.loadRacePage();
      }
  }
}