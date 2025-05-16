package client.ui.adminView.race;

import client.modelManager.ModelManager;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import shared.DTO.RaceDTO;
import shared.DTO.RaceTrackDTO;

public class CreateRaceVM {
  private final ModelManager model;

  // — Form fields —
  private final StringProperty raceName        = new SimpleStringProperty("");
  private final IntegerProperty horseCount     = new SimpleIntegerProperty(0);
  private final ObjectProperty<RaceTrackDTO> selectedTrack = new SimpleObjectProperty<>();
  // — Lists from model —
  private final ObservableList<RaceTrackDTO> availableTracks;
  private final ObservableList<RaceDTO>      raceQueue;
  // — Feedback —
  private final StringProperty message       = new SimpleStringProperty();
  public CreateRaceVM(ModelManager model) {
    this.model            = model;
    this.availableTracks  = model.getRaceTracksList();
    this.raceQueue        = model.getRaceList();

    // Refresh lists on startup
    model.getRaceTracks();
    model.getAllRaces();

    // Observe server responses for createRace
    model.createRaceSuccessProperty().addListener((obs, old, ok) -> {
      if (ok) {
        clearForm();
        model.getAllRaces();
      } else {
        message.set(model.createRaceMessageProperty().get());
      }
    });
  }

  // expose only the read-only view


  // optional setter so the VM can drive the UI
  public void setSelectedTrack(RaceTrackDTO t) {
    selectedTrack.set(t);
  }

  // — Property getters for binding —
  public StringProperty raceNameProperty()         { return raceName; }
  public IntegerProperty horseCountProperty()      { return horseCount; }
  public ObjectProperty<RaceTrackDTO> selectedTrackProperty() {
    return selectedTrack;
  }
  public ObservableList<RaceTrackDTO> getAvailableTracks() {
    return availableTracks;
  }
  public ObservableList<RaceDTO> getRaceQueue()    { return raceQueue; }
  public StringProperty messageProperty()          { return message; }

  // — Validation binding —
  public BooleanBinding canCreate() {
    return raceName.isNotEmpty()
            .and(selectedTrack.isNotNull())
            .and(horseCount.greaterThan(0));
  }

  // — Commands —
  public void createRace() {
    if (!canCreate().get()) return;
    model.createRace(
            raceName.get(),
            selectedTrack.get(),
            horseCount.get()
    );
  }

  private void clearForm() {
    raceName.set("");
    horseCount.set(0);
    selectedTrack.set(null);
    message.set("");
  }
}
