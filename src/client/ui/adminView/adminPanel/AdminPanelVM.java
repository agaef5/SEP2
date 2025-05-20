package client.ui.adminView.adminPanel;


import client.modelManager.ModelManager;
import client.ui.common.ViewModel;
import javafx.beans.property.*;
import shared.DTO.RaceDTO;
import shared.DTO.RaceState;


/**
 * ViewModel for the Admin Panel view.
 * Handles business logic and data processing for displaying upcoming race information.
 * Implements MessageListener to receive updates from the server.
 */
public class AdminPanelVM implements ViewModel //implements ViewModel, MessageListener
{
  // Controls whether buttons are enabled
  private final ObjectProperty<RaceDTO> nextRace = new SimpleObjectProperty<RaceDTO>(null);
  private final StringProperty raceInfoText = new SimpleStringProperty("Info on coming race");
  private ModelManager modelManager;


  public AdminPanelVM(ModelManager modelManager){
    this.modelManager = modelManager;

    nextRace.bind(modelManager.getNextRace());

    nextRace.addListener((observable, oldValue, newValue) -> {
      boolean hasUpcomingRace = newValue != null;
      updateRaceInfoText(hasUpcomingRace);
    });

    modelManager.getAllRaces();
    refreshRaceList();
  }

  public StringProperty raceInfoTextProperty() { return raceInfoText; }


  public void updateRaceInfoText(boolean hasUpcomingRace){
    if (hasUpcomingRace){
      String textToDisplay = "";
      RaceDTO race = nextRace.get();
      if(race.raceState().equals(RaceState.NOT_STARTED))
        textToDisplay += "Upcoming race:\n";
      else if (race.raceState().equals(RaceState.IN_PROGRESS)) {
        textToDisplay += "Ongoing race:\n";
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
}
