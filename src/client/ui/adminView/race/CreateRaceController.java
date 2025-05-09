package client.ui.adminView.race;

import client.ui.adminView.base.AdminViewBaseController;
import client.ui.common.ViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import shared.DTO.RaceDTO;
import shared.DTO.RaceTrackDTO;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Controller for the Create Race view.
 * Manages user interactions for creating new races and displaying the race queue.
 * Connects UI components to the corresponding ViewModel.
 */
public class CreateRaceController implements AdminViewBaseController
{
  /** Text field for entering the number of horses in the race */
  @FXML private TextField nrOfHorses;

  /** Choice box for selecting the race track */
  @FXML private ChoiceBox<RaceTrackDTO> raceTrack;

  /** Text field for entering the race name */
  @FXML private TextField raceName;

  /** Button to create a new race */
  @FXML private Button createRace;

  /** Button to navigate back to the admin panel */
  @FXML private Button back;

  /** ListView displaying the queue of upcoming races */
  @FXML private ListView<RaceDTO> raceQueueList;

  /** ViewModel that provides data and operations for this view */
  private CreateRaceVM viewModel;

  /** Controller that allows to control changing the view inside the main window*/
  private client.ui.adminView.AdminViewController adminViewController;

  /**
   * Default empty constructor required by FXML loader.
   */
  public CreateRaceController(){};

  /**
   * Initializes the controller with the provided ViewModel.
   * Sets up bindings between UI components and ViewModel properties,
   * configures cell rendering for the race queue list, and attaches event handlers.
   *
   * @param createRaceVM The ViewModel that provides data and operations for this view
   */
  public void initialize(ViewModel createRaceVM)
  {
    viewModel = (CreateRaceVM) createRaceVM;

    // Bind the choice box to the available racetracks in the ViewModel
    raceTrack.setItems(this.viewModel.getAvailableRaceTracks());
    this.viewModel.selectedRaceTrackProperty().bind(raceTrack.getSelectionModel().selectedItemProperty());

    // Configure the race queue ListView with data from the ViewModel
    raceQueueList.setItems(this.viewModel.getRaceQueue());
    raceQueueList.setCellFactory(param -> new ListCell<>() {
      @Override
      protected void updateItem(RaceDTO race, boolean empty) {
        super.updateItem(race, empty);
        if (empty || race == null) {
          setText(null);
        } else {
          setText(race.name() + " - Track: " + race.raceTrack().name());
        }
      }
    });

    // Update the horse count in the ViewModel when the text field changes
    nrOfHorses.textProperty().addListener((obs, oldVal, newVal) -> {
      try {
        this.viewModel.horseCountProperty().set(Integer.parseInt(newVal));
      } catch (NumberFormatException e) {
        this.viewModel.horseCountProperty().set(0); // Default to 0 on invalid input
      }
    });

    // Bind the race name text field to the ViewModel property
    raceName.textProperty().bindBidirectional(this.viewModel.raceNameProperty());
  }

  /**
   * Allows to change tabs inside the main window within the tab
   *
   * @param adminViewController - the main window controller that changes tabs
   */
  @Override
  public void setAdminViewController(client.ui.adminView.AdminViewController adminViewController) {
    if(adminViewController != null)
      this.adminViewController = adminViewController;
  }

  /**
   * Handles the Create Race button click event.
   * Validates input and creates a new race if valid, otherwise shows an alert.
   *
   * @throws SQLException If there is an error accessing the database
   */
  @FXML private void onCreateRaceClicked() throws SQLException
  {
    if(viewModel.isValid()){
      viewModel.createRace();
    }
    else {
      showAlert("Incorrect input", "Make sure all the fields are filled.");
    }
  }

  /**
   * Handles the Back button click event.
   * Navigates back to the admin panel view.
   *
   * @throws IOException If there is an error loading the admin panel view
   */
  @FXML private void onBackClicked() throws IOException
  {
    FXMLLoader loader = new FXMLLoader(getClass().getResource(
        "/client/ui/adminView/adminPanel/AdminPanel.fxml"));
    Parent root = loader.load();
    Stage stage = (Stage) back.getScene().getWindow();
    stage.setScene(new Scene(root));
  }




  /**
   * Displays an alert dialog with the specified title and content.
   * Used for showing validation errors and other notifications.
   *
   * @param title The title of the alert dialog
   * @param content The content message to display in the alert
   */
  private void showAlert(String title, String content)
  {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);
    alert.showAndWait();
  }
}