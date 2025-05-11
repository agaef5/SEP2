package client.ui.adminView.adminPanel;

import client.networking.SocketService;
import client.networking.race.RaceClient;
import client.ui.MessageListener;
import client.ui.common.ViewModel;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import shared.DTO.RaceDTO;
import shared.race.GetRaceListResponse;

import java.util.List;

/**
 * ViewModel for the Admin Panel view.
 * Handles business logic and data processing for displaying upcoming race information.
 * Implements MessageListener to receive updates from the server.
 */
public class AdminPanelVM implements ViewModel, MessageListener
{
  /** Client for communicating with race-related server endpoints */
  private RaceClient raceClient;

  /** Service for socket communication with the server */
  private SocketService socketService;

  /** JSON parser for handling server responses */
  private Gson gson;

  /** Observable property containing information about the next race */
  private StringProperty nextRaceInfo = new SimpleStringProperty("No planned races");

  /**
   * Constructs the ViewModel with dependencies and initializes data.
   *
   * @param raceClient Client for race-related server operations
   * @param socketService Service for socket communication with the server
   */
  public AdminPanelVM(RaceClient raceClient, SocketService socketService)
  {
    this.raceClient = raceClient;
    this.socketService = socketService;
    this.gson = new Gson();

    // Register this ViewModel as a listener for socket messages
    this.socketService.addListener(this);

    // Request the current race list from the server
    refreshRaceList();
  }

  /**
   * Requests the current race list from the server.
   * This will trigger an update via the MessageListener when the response is received.
   */
  private void refreshRaceList()
  {
    raceClient.getRaceList();
  }

  /**
   * Gets the observable property containing information about the next race.
   * This property can be bound to UI elements to display race information.
   *
   * @return StringProperty containing formatted race information
   */
  public StringProperty getNextRaceInfo()
  {
    return nextRaceInfo;
  }

  /**
   * Handles messages received from the server via the socket connection.
   * Processes different message types and updates the ViewModel state accordingly.
   *
   * @param type The type of message received
   * @param payload The JSON payload containing the message data
   */
  @Override public void update(String type, String payload)
  {
    if ("getRaceList".equals(type))
    {
      GetRaceListResponse response = gson.fromJson(payload, GetRaceListResponse.class);
      updateRaceInfo(response.races());
    }
  }

  /**
   * Updates the next race information based on the received race list.
   * If races are available, formats information about the first race in the list.
   * Updates must be performed on the JavaFX application thread.
   *
   * @param races List of races received from the server
   */
  private void updateRaceInfo(List<RaceDTO> races)
  {
    Platform.runLater(() -> {
      if (races != null && !races.isEmpty()) {
        // Get the first race in the queue (assume it's sorted by time)
        RaceDTO nextRace = races.get(0);

        // Format the race information
        String info = String.format("Next race: %s - Track: %s - Horses: %d",
            nextRace.name(),
            nextRace.raceTrack().name(),
            nextRace.horses().size());

        nextRaceInfo.set(info);
      } else {
        nextRaceInfo.set("No upcoming races");
      }
    });
  }
}