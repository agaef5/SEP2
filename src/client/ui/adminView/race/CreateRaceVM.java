package client.ui.adminView.race;

import client.modelManager.ModelManager;
import client.networking.SocketService;
import client.networking.race.RaceClient;
import client.ui.common.ViewModel;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shared.DTO.RaceDTO;
import shared.DTO.RaceTrackDTO;
import shared.race.*;

/**
 * ViewModel for the Create Race view.
 *
 * Manages the data and operations related to race creation and displaying the race queue.
 * Communicates with the ModelManager to send and receive data updates.
 */
public class CreateRaceVM implements ViewModel {

  /** Model manager for managing all necessary operations */
  private final ModelManager modelManager;

  /** Observable property for the race name */
  private StringProperty raceName = new SimpleStringProperty();

  /** Observable property for the number of horses in the race */
  private IntegerProperty horseCount = new SimpleIntegerProperty();

  /** Observable property for the selected race track */
  private ObjectProperty<RaceTrackDTO> selectedRaceTrack = new SimpleObjectProperty<>();

  /** Observable list containing all available race tracks */
  private ObservableList<RaceTrackDTO> availableRaceTracks;

  /** Observable list containing the queue of upcoming races */
  private ObservableList<RaceDTO> raceQueue;

  /** Property bound to the message label in the view */
  private StringProperty messageLabel = new SimpleStringProperty();

  /**
   * Constructs the ViewModel with necessary dependencies and initializes data.
   *
   * @param modelManager the ModelManager for accessing race data and operations
   */
  public CreateRaceVM(ModelManager modelManager) {
    this.modelManager = modelManager;

    modelManager.getRaceTracksList();
    modelManager.getRaceList();

    availableRaceTracks = modelManager.getRaceTracksList();
    raceQueue = modelManager.getRaceList();

    // Bind message label to ModelManager's race creation message
    messageLabel.bind(modelManager.createRaceMessageProperty());

    // Initial data fetching
    modelManager.getRaceTracks();
    modelManager.getAllHorses();
    modelManager.getAllRaces();
  }

  /**
   * Formats a RaceTrackDTO for display in the choice box.
   *
   * @param track the race track to format
   * @return a user-friendly string representation of the track
   */
  public String formatRaceTrack(RaceTrackDTO track) {
    if (track == null) return "";
    return track.name() + " - " + track.length() + "m";
  }

  /** @return Property containing the race name */
  public StringProperty raceNameProperty() {
    return raceName;
  }

  /** @return Property containing the number of horses in the race */
  public IntegerProperty horseCountProperty() {
    return horseCount;
  }

  /** @return Property containing the selected race track */
  public ObjectProperty<RaceTrackDTO> selectedRaceTrackProperty() {
    return selectedRaceTrack;
  }

  /** @return Observable list of all race tracks */
  public ObservableList<RaceTrackDTO> getAvailableRaceTracks() {
    return availableRaceTracks;
  }

  /** @return Observable list of races in the queue */
  public ObservableList<RaceDTO> getRaceQueue() {
    return raceQueue;
  }

  /** @return Property containing the message to display to the user */
  public StringProperty getMessageLabel() {
    return messageLabel;
  }

  /**
   * Validates the input fields for creating a race.
   *
   * @return true if all required fields are valid, false otherwise
   */
  public boolean isValid() {
    return raceName.get() != null && !raceName.get().isBlank()
            && selectedRaceTrack.get() != null && horseCount.get() > 0;
  }

  /**
   * Creates a new race with the current form values.
   * Only executes if the input is valid.
   * Clears the input fields after creation.
   */
  public void createRace() {
    if (!isValid()) {
      return;
    }

    modelManager.createRace(raceName.get(), selectedRaceTrack.get(), horseCount.get());

    // Clear input fields after creating
    raceName.set("");
    horseCount.set(0);
  }
}
