package client.ui.adminView.horseList;

import client.networking.SocketService;
import client.networking.racers.RacersClient;
import client.ui.MessageListener;
import client.ui.util.RacerDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.model.Horse;
import server.model.Racer;
import shared.*;

import java.util.ArrayList;

public class CreateEditRacerVM implements MessageListener {

  private final RacersClient racersClient;
  private final SocketService socketService;
  private final ObservableList<Racer> racerList = FXCollections.observableArrayList();

  private final StringProperty racerType = new SimpleStringProperty();
  private final StringProperty racerName = new SimpleStringProperty();
  private final IntegerProperty speedMin = new SimpleIntegerProperty();
  private final IntegerProperty speedMax = new SimpleIntegerProperty();
  private final Gson gson;

  private Racer selectedRacer;

  public CreateEditRacerVM(RacersClient client,SocketService socketService) {
    this.racersClient = client;
    this.socketService = socketService;
    this.socketService.addListener(this);
    racersClient.getRacerList();
    this.gson = new GsonBuilder()
        .registerTypeAdapter(Racer.class, new RacerDeserializer())
        .create();
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
        -1, // temporary ID
        racerName.get(),
        speedMin.get(),
        speedMax.get()
    );

    newRacer.setType(racerType.get());
    CreateRacerRequest createRacerRequest = new CreateRacerRequest(racerType.get(), racerName.get(), speedMin.get(), speedMax.get());
    racersClient.createRacer(createRacerRequest);
    racersClient.getRacerList();
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

  public void updateRacerList(RacerListResponse racerListResponse){
    if(racerListResponse == null) return;

    Platform.runLater(() -> {
      System.out.println("Platform.runLater: racers = " + racerListResponse.racerList());
      ArrayList<Horse> horseList = new ArrayList<>();
      for (Racer racer : racerListResponse.racerList()) {
        if (racer instanceof Horse horse) {
          horseList.add(horse);
        }
      }
      racerList.setAll(horseList);
      System.out.println("List updated");
    });
  }

  @Override
  public void update(String type, String payload){
    System.out.println("Message received: " + type);
    switch (type) {
      case "getRacerList":
        RacerListResponse racerListResponse = gson.fromJson(payload, RacerListResponse.class);
        System.out.println("Parsed racers: " + racerListResponse.racerList());
        updateRacerList(racerListResponse);
        break;
      case "createRacer":
        CreateRacerResponse createRacerResponse = gson.fromJson(payload, CreateRacerResponse.class);
          handleCreateRacerResponse(createRacerResponse);
        break;
      case "editRacer":
        break;
    }
  }

  private void handleCreateRacerResponse(CreateRacerResponse createRacerResponse)
  {
    racersClient.getRacerList();
    if (createRacerResponse.racer() instanceof Horse horse){
      setSelectedRacer(horse);
    }
  }
}




