package client.ui.adminView.race;

import client.networking.SocketService;
import client.networking.race.RaceClient;
import client.ui.MessageListener;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.model.Horse;
import server.model.Race;
import server.model.RaceTrack;

import java.sql.SQLException;


public class CreateRaceVM implements MessageListener
{
  private final SocketService socketService;
  private StringProperty raceName = new SimpleStringProperty();
  private IntegerProperty horseCount = new SimpleIntegerProperty();
  private ObjectProperty<RaceTrack> selectedRaceTrack = new SimpleObjectProperty<>();
  private ObservableList<RaceTrack> availableRaceTracks = FXCollections.observableArrayList();

  public CreateRaceVM(RaceClient raceClient,SocketService socketService)
  {
    this.socketService=socketService;
    this.socketService.addListener(this);
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
        && selectedRaceTrack.get() != null && horseCount.get() > 0;
  }

  //TODO main function, but needs filling








  public void createRace()
  {
  }

  @Override public void update(String type, String payload)
  {

  }

  @Override public void update(Object message)
  {

  }
}



