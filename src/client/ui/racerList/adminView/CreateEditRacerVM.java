package client.ui.racerList.adminView;

import client.networking.racers.RacersClient;
import client.ui.MessageListener;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.model.Horse;
import server.model.Racer;
import shared.Respond;
import shared.RacerListResponse;

public class CreateEditRacerVM implements MessageListener {

  private final RacersClient racersClient;
  private final ObservableList<Racer> racerList = FXCollections.observableArrayList();

  private final StringProperty racerType = new SimpleStringProperty();
  private final StringProperty racerName = new SimpleStringProperty();
  private final IntegerProperty speedMin = new SimpleIntegerProperty();
  private final IntegerProperty speedMax = new SimpleIntegerProperty();

  private Racer selectedRacer;

  public CreateEditRacerVM(RacersClient client) {
    this.racersClient = client;
    this.racersClient.addListener(this);
    racersClient.getRacerList();
  }

  public Property<String> racerTypeProperty() { return racerType; }
  public Property<String> racerNameProperty() { return racerName; }
  public IntegerProperty speedMinProperty() { return speedMin; }
  public IntegerProperty speedMaxProperty() { return speedMax; }

  public ObservableList<Racer> getRacerList() {
    return racerList;
  }

  public void setSelectedRacer(Racer newVal) {
    this.selectedRacer = newVal;
    if (newVal != null) {
      racerType.set(newVal.getType());
      racerName.set(newVal.getName());
      speedMin.set(newVal.getSpeedMin());
      speedMax.set(newVal.getSpeedMax());

    }
  }
  public void addRacer() {
    Racer newRacer = new Horse(
        -1, // tijdelijk ID
        racerName.get(),
        speedMin.get(),
        speedMax.get()

    );
    newRacer.setType(racerType.get());
    racersClient.createRacer(newRacer);
  }

  public void updateRacer() {
    if (selectedRacer != null) {
      selectedRacer.setType(racerType.get());
      selectedRacer.setName(racerName.get());
      selectedRacer.setSpeedMin(speedMin.get());
      selectedRacer.setSpeedMax(speedMax.get());
      racersClient.updateRacer(selectedRacer);
    }
  }

  public void removeRacer() {
    if (selectedRacer != null) {
      racersClient.deleteRacer(selectedRacer);
    }
  }

  @Override
  public void update(Object message) {
    if (message instanceof Respond respond && respond.payload() instanceof RacerListResponse response) {
      Platform.runLater(() -> racerList.setAll(response.racers()));
    }
  }
}
