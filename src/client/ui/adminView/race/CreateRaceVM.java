package client.ui.adminView.race;

import client.networking.SocketService;
import client.networking.race.RaceClient;
import client.ui.MessageListener;
import client.ui.common.ViewModel;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shared.DTO.RaceDTO;
import shared.DTO.RaceTrackDTO;
import shared.race.*;

/**
 * ViewModel for the Create Race view.
 * Manages the data and operations related to race creation and displaying the race queue.
 * Handles communication with the server via RaceClient and processes responses.
 * Implements MessageListener to receive updates from the server.
 */
public class CreateRaceVM implements ViewModel, MessageListener
{
  /** Service for socket communication with the server */
  private final SocketService socketService;

  /** Client for communicating with race-related server endpoints */
  private final RaceClient raceClient;

  /** JSON parser for handling server responses */
  private final Gson gson;

  /** Observable property for the race name */
  private StringProperty raceName = new SimpleStringProperty();

  /** Observable property for the number of horses in the race */
  private IntegerProperty horseCount = new SimpleIntegerProperty();

  /** Observable property for the selected race track */
  private ObjectProperty<RaceTrackDTO> selectedRaceTrack = new SimpleObjectProperty<>();

  /** Observable list containing all available race tracks */
  private ObservableList<RaceTrackDTO> availableRaceTracks = FXCollections.observableArrayList();

  /** Observable list containing the queue of upcoming races */
  private ObservableList<RaceDTO> raceQueue = FXCollections.observableArrayList();

  /**
   * Constructs the ViewModel with necessary dependencies and initializes data.
   *
   * @param raceClient Client for race-related server operations
   * @param socketService Service for socket communication with the server
   */
  public CreateRaceVM(RaceClient raceClient, SocketService socketService)
  {
    this.raceClient = raceClient;
    this.gson = new Gson();
    this.socketService = socketService;
    this.socketService.addListener(this);

    // Request race tracks when the ViewModel is initialized
    GetRaceTracksRequest request = new GetRaceTracksRequest();
    raceClient.getRaceTracks(request);

    // Request the race list to initialize the race queue
    raceClient.getRaceList();
  }

  /**
   * Gets the observable property for the race name.
   * @return Property containing the race name
   */
  public StringProperty raceNameProperty()
  {
    return raceName;
  }

  /**
   * Gets the observable property for the number of horses.
   * @return Property containing the number of horses in the race
   */
  public IntegerProperty horseCountProperty()
  {
    return horseCount;
  }

  /**
   * Gets the observable property for the selected race track.
   * @return Property containing the selected race track
   */
  public ObjectProperty<RaceTrackDTO> selectedRaceTrackProperty()
  {
    return selectedRaceTrack;
  }

  /**
   * Gets the observable list containing all available race tracks.
   * @return Observable list of all race tracks
   */
  public ObservableList<RaceTrackDTO> getAvailableRaceTracks()
  {
    return availableRaceTracks;
  }

  /**
   * Gets the observable list containing the queue of upcoming races.
   * @return Observable list of races in the queue
   */
  public ObservableList<RaceDTO> getRaceQueue(){
    return raceQueue;
  }

  /**
   * Validates the input fields for creating a race.
   * @return true if all required fields are valid, false otherwise
   */
  public boolean isValid()
  {
    return raceName.get() != null && !raceName.get().isBlank()
        && selectedRaceTrack.get() != null && horseCount.get() > 0;
  }

  /**
   * Creates a new race with the current form values.
   * Only executes if the input is valid.
   * Clears the input fields after successful creation.
   */
  public void createRace()
  {
    if (!isValid())
    {
      return;
    }
    CreateRaceRequest request = new CreateRaceRequest(
        raceName.get(),
        selectedRaceTrack.get(),
        horseCount.get()
    );
    raceClient.createRace(request);

    // Clear input fields after creating
    raceName.set("");
    horseCount.set(0);
  }

  /**
   * Handles messages received from the server via the socket connection.
   * Processes different message types and updates the ViewModel state accordingly.
   *
   * @param type The type of message received
   * @param payload The JSON payload containing the message data
   */
  @Override
  public void update(String type, String payload) {
    switch (type) {
      case "getRaceTracks":
        GetRaceTrackResponse trackResponse = gson.fromJson(payload, GetRaceTrackResponse.class);
        updateAvailableRaceTracks(trackResponse);
        break;
      case "getRaceList":
        GetRaceListResponse raceListResponse = gson.fromJson(payload, GetRaceListResponse.class);
        handleRaceList(raceListResponse);
        break;
      case "createRace":
        // Add null check for payload
        if (payload != null && !payload.isEmpty()) {
          CreateRaceResponse createRaceResponse = gson.fromJson(payload, CreateRaceResponse.class);
          if (createRaceResponse != null) {
            handleCreateRaceResponse(createRaceResponse);
            raceClient.getRaceList();
            if (createRaceResponse.Race() != null) {
              RaceDTO newRace = gson.fromJson(createRaceResponse.Race().toString(), RaceDTO.class);
              setSelectedRace(newRace);
            }
          } else {
            System.out.println("Received null CreateRaceResponse");
            raceClient.getRaceList(); // Still refresh the race list
          }
        } else {
          System.out.println("Received empty payload for createRace");
          raceClient.getRaceList(); // Still refresh the race list
        }
        break;
    }
  }

  /**
   * Sets the specified race as the selected race.
   * This method is currently a placeholder.
   *
   * @param newRace The race to select
   */
  private void setSelectedRace(RaceDTO newRace)
  {
    // This is a placeholder method, implementation may be added in the future
  }

  /**
   * Handles the response after creating a race.
   * Logs success or failure to the console.
   *
   * @param createRaceResponse Response object from the create race operation
   */
  private void handleCreateRaceResponse(CreateRaceResponse createRaceResponse) {
    Platform.runLater(() -> {
      // Add null check here
      if (createRaceResponse != null && createRaceResponse.Race() != null) {
        System.out.println("Race created successfully");
      } else {
        System.out.println("Failed to create race");
      }
    });
  }

  /**
   * Updates the available race tracks list with data received from the server.
   * Updates must be performed on the JavaFX application thread.
   *
   * @param response Response object containing the list of race tracks
   */
  private void updateAvailableRaceTracks(GetRaceTrackResponse response)
  {
    Platform.runLater(()->
        availableRaceTracks.setAll(response.raceTracks()));
  }

  /**
   * Updates the race queue list with data received from the server.
   * Updates must be performed on the JavaFX application thread.
   *
   * @param response Response object containing the list of races
   */
  private void handleRaceList(GetRaceListResponse response)
  {
    Platform.runLater(() -> {
      // Update the race queue with the received races
      raceQueue.clear();
      if (response.races() != null) {
        raceQueue.addAll(response.races());
      }
      System.out.println("Race queue updated with " + raceQueue.size() + " races");
    });
  }
}