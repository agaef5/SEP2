package client.ui.adminView.adminPanel;

import client.ui.adminView.AdminTabbedWindowController;
import client.ui.adminView.BaseAdminController;
import client.ui.adminView.BaseViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

/**
 * Controller class for the admin panel view.
 * Handles user interactions and manages the display of upcoming race information.
 */
public class AdminPanelController implements BaseAdminController
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
  private AdminTabbedWindowController adminTabbedWindowController;

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
  public void initialize(BaseViewModel viewModel)
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


  /**
   * Allows to change tabs inside the main window within the tab
   *
   * @param adminTabbedWindowController - the main window controller that changes tabs
   */
  @Override
  public void setTabbedWindowController(AdminTabbedWindowController adminTabbedWindowController) {
    if(adminTabbedWindowController != null)
      this.adminTabbedWindowController = adminTabbedWindowController;
  }


  /**
   * Loads a new scene and sets it as the current scene in the application window.
   *
   * @param event - "event" triggered by clicking on one of the buttons on the AdminController
   */
  public void loadPage(javafx.event.ActionEvent event) throws IOException {

    if(event.getSource() == addHorse){
      adminTabbedWindowController.loadHorsePage();
    }

    if(event.getSource() == addRace){
      adminTabbedWindowController.loadRacePage();
    }

//    try
//    {
//      FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
//      Parent root = loader.load();
//      Stage stage = (Stage) addRace.getScene().getWindow();
//      stage.setScene(new Scene(root));
//    } catch (IOException e)
//    {
//      e.printStackTrace();
//    }
  }
}