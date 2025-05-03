package client.ui.racerList.adminView.race;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.model.Horse;
import server.model.Race;
import server.model.RaceTrack;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CreateRaceVM
{

  private StringProperty raceName = new SimpleStringProperty();
  private IntegerProperty horseCount = new SimpleIntegerProperty();
  private ObjectProperty<RaceTrack> selectedRaceTrack = new SimpleObjectProperty<>();
  private ObservableList<RaceTrack> availableRaceTracks = FXCollections.observableArrayList();

  // should this come from server??!!
  private ObservableList<Horse> availableHorses = FXCollections.observableArrayList();

  public CreateRaceVM()
  {
    //fill in test data
    availableRaceTracks.addAll( /* where from? */);
    availableHorses.addAll( /* where from? */);
  }

  // Properties
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
        && horseCount.get() <= getAvailableHorses().size();
  }

  public ObservableList<Horse> getAvailableHorses() {
    return availableHorses.filtered(h -> !h.isInRace());
  }

  // main function, but needs filling
  public void createRace()
  {
   //
  }

  private List<Horse> pickRandomHorses(int count)
  {
    List<Horse> freeHorses = availableHorses.stream()
        .filter(h -> !h.isInRace())
        .collect(Collectors.toList());

    //nice inbuilt api function to random shuffle things on lists
    Collections.shuffle(freeHorses);
    return freeHorses.subList(0, count);
  }
}

