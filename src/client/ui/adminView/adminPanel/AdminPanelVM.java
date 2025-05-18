package client.ui.adminView.adminPanel;


import client.modelManager.ModelManager;
import client.networking.SocketService;
import client.networking.race.RaceClient;
import client.ui.common.MessageListener;
import client.ui.common.ViewModel;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.property.*;
import shared.DTO.RaceDTO;
import shared.DTO.RaceState;
import shared.race.GetRaceListResponse;


import java.util.List;


/**
 * ViewModel for the Admin Panel view.
 * Handles business logic and data processing for displaying upcoming race information.
 * Implements MessageListener to receive updates from the server.
 */
public class AdminPanelVM implements ViewModel //implements ViewModel, MessageListener
{
  private RaceClient raceClient;
  private SocketService socketService;
  private StringProperty nextRaceInfo = new SimpleStringProperty("No planned races");

  // Controls whether buttons are enabled
  private final ObjectProperty<RaceDTO> nextRace = new SimpleObjectProperty<RaceDTO>(null);
  private final StringProperty raceInfoText = new SimpleStringProperty("Info on coming race");
  private ModelManager modelManager;


  public AdminPanelVM(ModelManager modelManager){
    this.modelManager = modelManager;

    nextRace.bind(modelManager.nextRaceProperty());

    nextRace.addListener((observable, oldValue, newValue) -> {
      boolean hasUpcomingRace = newValue != null;
      updateRaceInfoText(hasUpcomingRace);
    });

    refreshRaceList();
  }

  public StringProperty raceInfoTextProperty() { return raceInfoText; }


  public void updateRaceInfoText(boolean hasUpcomingRace){
    if (hasUpcomingRace){
      String textToDisplay = "";
      RaceDTO race = nextRace.get();
      if(race.raceState().equals(RaceState.NOT_STARTED))
        textToDisplay += "Upcoming race:";
      else if (race.raceState().equals(RaceState.IN_PROGRESS)) {
        textToDisplay += "Ongoing race:";
      }
      textToDisplay += race.name() +"\n"
              + race.raceTrack().name() + ", " + race.raceTrack().location();

      raceInfoText.set(textToDisplay);
    } else {
      raceInfoText.set("No race in queue");
    }
  }

  /**
   * Requests the current race list from the server.
   * This will trigger an update via the MessageListener when the response is received.
   */
  private void refreshRaceList()
  {
    modelManager.getRaceList();
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
}
