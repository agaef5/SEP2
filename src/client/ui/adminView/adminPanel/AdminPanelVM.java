package client.ui.adminView.adminPanel;

import client.modelManager.ModelManager;
import client.ui.common.ViewModel;
import javafx.beans.property.*;
import shared.DTO.RaceDTO;
import shared.DTO.RaceState;

/**
 * ViewModel for the Admin Panel view.
 *
 * Manages business logic related to upcoming races,
 * provides race info as a property, and listens for changes in race data.
 */
public class AdminPanelVM implements ViewModel { // implements ViewModel, MessageListener

  /** Holds the next upcoming race received from the model */
  private final ObjectProperty<RaceDTO> nextRace = new SimpleObjectProperty<>(null);

  /** Text property bound to the label in the UI displaying race info */
  private final StringProperty raceInfoText = new SimpleStringProperty("Info on coming race");

  /** Reference to the ModelManager used to fetch race data */
  private final ModelManager modelManager;

  /**
   * Constructs the AdminPanelVM with the given model manager.
   * Binds to the next race and sets up a listener to update display text when race changes.
   *
   * @param modelManager the model manager for accessing shared data
   */
  public AdminPanelVM(ModelManager modelManager) {
    this.modelManager = modelManager;

    nextRace.bind(modelManager.getNextRace());

    nextRace.addListener((observable, oldValue, newValue) -> {
      boolean hasUpcomingRace = newValue != null;
      updateRaceInfoText(hasUpcomingRace);
    });

    modelManager.getAllRaces();
    refreshRaceList();
  }

  /**
   * Property accessor for the race information text.
   *
   * @return the race info StringProperty
   */
  public StringProperty raceInfoTextProperty() {
    return raceInfoText;
  }

  /**
   * Updates the race info text shown in the UI based on the current upcoming race.
   *
   * @param hasUpcomingRace true if a race is queued or in progress; false otherwise
   */
  public void updateRaceInfoText(boolean hasUpcomingRace) {
    if (hasUpcomingRace) {
      String textToDisplay = "";
      RaceDTO race = nextRace.get();
      if (race.raceState().equals(RaceState.NOT_STARTED))
        textToDisplay += "Upcoming race:\n";
      else if (race.raceState().equals(RaceState.IN_PROGRESS))
        textToDisplay += "Ongoing race:\n";

      textToDisplay += race.name() + "\n" +
              race.raceTrack().name() + ", " + race.raceTrack().location();

      raceInfoText.set(textToDisplay);
    } else {
      raceInfoText.set("No race in queue");
    }
  }

  /**
   * Requests the current race list from the server.
   * Triggers updates to the bound nextRace property.
   */
  private void refreshRaceList() {
    modelManager.getRaceList();
  }
}
