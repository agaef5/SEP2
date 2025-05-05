package client.ui.adminView.race;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.model.Horse;
import server.model.Race;
import server.model.RaceTrack;

import java.sql.SQLException;


public class CreateRaceVM
{

  private StringProperty raceName = new SimpleStringProperty();
  private IntegerProperty horseCount = new SimpleIntegerProperty();
  private ObjectProperty<RaceTrack> selectedRaceTrack = new SimpleObjectProperty<>();
  private ObservableList<RaceTrack> availableRaceTracks = FXCollections.observableArrayList();

  public CreateRaceVM()
  {
    //fill in test data
    availableRaceTracks.addAll( /* where from? */
    new RaceTrack("Dirt Track", 1000, "Texas"),
    new RaceTrack("Green Field", 2000, "England"));
      }

  // getters for Properties
  public StringProperty raceNameProperty()
  {
    return raceName;
  }

  public IntegerProperty horseCountProperty()
  {
    return horseCount;
  }

  public ObjectProperty<RaceTrack> selectedRaceTrackProperty()
  {
    return selectedRaceTrack;
  }

  public ObservableList<RaceTrack> getAvailableRaceTracks()
  {
    return availableRaceTracks;
  }

  // Validation
  public boolean isValid()
  {
    return raceName.get() != null && !raceName.get().isBlank()
        && selectedRaceTrack.get() != null && horseCount.get() > 0
        && horseCount.get() >0;
  }

  // main function, but needs filling
  public void createRace()
  {
    try {
      Race race = new Race(
          raceName.get(),
          horseCount.get()
      );

      race.setRaceTrack(selectedRaceTrack.get());

      RaceManager.getInstance().queueRace(race);

    } catch (SQLException e) {
      e.printStackTrace();

    }
  }
}



