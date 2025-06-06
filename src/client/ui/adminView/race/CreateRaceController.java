package client.ui.adminView.race;

import client.ui.common.Controller;
import client.ui.common.ViewModel;
import client.ui.navigation.MainWindowController;
import client.ui.util.ErrorHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import shared.DTO.RaceDTO;
import shared.DTO.RaceTrackDTO;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Controller for the Create Race view.
 *
 * Manages user interactions for creating new races and displaying the race queue.
 * Connects UI components to the corresponding ViewModel.
 */
public class CreateRaceController implements Controller {

  /** Text field for entering the number of horses in the race */
  @FXML private TextField nrOfHorses;

  /** Choice box for selecting the race track */
  @FXML private ChoiceBox<RaceTrackDTO> raceTrack;

  /** Text field for entering the race name */
  @FXML private TextField raceName;

  /** Button to create a new race */
  @FXML private Button createRace;

  /** ListView displaying the queue of upcoming races */
  @FXML private ListView<RaceDTO> raceQueueList;

  /** Label to display messages */
  @FXML private Label messageLabel;

  /** ViewModel that provides data and operations for this view */
  private CreateRaceVM viewModel;

  /** Controller that allows switching views inside the main window */
  private MainWindowController mainWindowController;

  /**
   * Default empty constructor required by FXML loader.
   */
  public CreateRaceController() {}

  /**
   * Initializes the controller with the provided ViewModel.
   * Sets up bindings between UI components and ViewModel properties,
   * configures cell rendering for the race queue list, and attaches event handlers.
   *
   * @param createRaceVM the ViewModel that provides data and operations for this view
   */
  @Override
  public void initialize(ViewModel createRaceVM) {
    viewModel = (CreateRaceVM) createRaceVM;

    // Bind the choice box to the available racetracks in the ViewModel
    raceTrack.setItems(this.viewModel.getAvailableRaceTracks());
    viewModel.selectedRaceTrackProperty().bind(raceTrack.getSelectionModel().selectedItemProperty());

    // Format how each race track is shown in the dropdown
    raceTrack.setConverter(new StringConverter<>() {
      @Override
      public String toString(RaceTrackDTO track) {
        return viewModel.formatRaceTrack(track);
      }

      @Override
      public RaceTrackDTO fromString(String string) {
        return null; // not needed for ChoiceBox
      }
    });

    // Configure the race queue ListView with data from the ViewModel
    raceQueueList.setItems(viewModel.getRaceQueue());
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

    // Bind the message label
    messageLabel.textProperty().bind(viewModel.getMessageLabel());

    // Set the action for the Create Race button
    createRace.setOnAction(event -> {
      onCreateRaceClicked();
    });
  }

  /**
   * Allows switching tabs inside the main window.
   *
   * @param mainWindowController the main window controller that changes views
   */
  @Override
  public void setWindowController(MainWindowController mainWindowController) {
    if (mainWindowController != null)
      this.mainWindowController = mainWindowController;
  }

  /**
   * Handles the Create Race button click event.
   * Validates input and creates a new race if valid; otherwise shows an alert.
   */
  @FXML private void onCreateRaceClicked() {
    if (viewModel.isValid()) {
      viewModel.createRace();
    } else {
      ErrorHandler.showAlert("Incorrect input", "Make sure all the fields are filled.");
    }
  }
}
