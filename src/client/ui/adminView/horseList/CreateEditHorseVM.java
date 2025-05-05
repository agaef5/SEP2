package client.ui.adminView.horseList;

import client.networking.SocketService;
import client.networking.horses.HorsesClient;
import client.ui.MessageListener;
import client.ui.util.HorseDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.model.Horse;

import shared.*;

import java.util.ArrayList;

public class CreateEditHorseVM implements MessageListener {

  private final HorsesClient horseClient;
  private final SocketService socketService;
  private final ObservableList<Horse> horseList = FXCollections.observableArrayList();

  private final StringProperty horseName = new SimpleStringProperty();
  private final IntegerProperty speedMin = new SimpleIntegerProperty();
  private final IntegerProperty speedMax = new SimpleIntegerProperty();
  private final Gson gson;

  private Horse selectedHorse;

  public CreateEditHorseVM(HorsesClient client,SocketService socketService) {
    this.horseClient = client;
    this.socketService = socketService;
    this.socketService.addListener(this);
    horseClient.getHorseList();
    this.gson = new GsonBuilder()
        .registerTypeAdapter(Horse.class, new HorseDeserializer())
        .create();
  }

  public Property<String> horseNameProperty() { return horseName; }
  public IntegerProperty speedMinProperty() { return speedMin; }
  public IntegerProperty speedMaxProperty() { return speedMax; }

  public ObservableList<Horse> getHorseList() {
    return horseList;
  }

  public void setSelectedHorse(Horse newVal) {
    this.selectedHorse = newVal;
    if (newVal != null) {
      horseName.set(newVal.getName());
      speedMin.set(newVal.getSpeedMin());
      speedMax.set(newVal.getSpeedMax());
    }
  }

   public void addHorse() {
    Horse newHorse = new Horse(
        -1, // temporary ID
        horseName.get(),
        speedMin.get(),
        speedMax.get()
    );

    CreateHorseRequest createHorseRequest = new CreateHorseRequest(horseName.get(), speedMin.get(), speedMax.get());
    horseClient.createHorse(createHorseRequest);
    horseClient.getHorseList();
  }

  public void updateHorse() {
    if (selectedHorse != null) {
      selectedHorse.setName(horseName.get());
      selectedHorse.setSpeedMin(speedMin.get());
      selectedHorse.setSpeedMax(speedMax.get());
      horseClient.updateHorse(selectedHorse);
    }
  }

  public void removeHorse() {
    if (selectedHorse != null) {
      horseClient.deleteHorse(selectedHorse);
    }
  }

  public void updateHorseList(HorseListResponse horseListResponse){
    if(horseListResponse == null) return;

    Platform.runLater(() -> {
      System.out.println("Platform.runLater: horses = " + horseListResponse.horseList());
      ArrayList<Horse> newHorseList = new ArrayList<>();
      for (Horse horse : horseListResponse.horseList()) {
        newHorseList.add(horse);
      }
      horseList.setAll(newHorseList);
      System.out.println("List updated");
    });
  }

  @Override
  public void update(String type, String payload){
    System.out.println("Message received: " + type);
    switch (type) {
      case "getHorseList":
        HorseListResponse horseListResponse = gson.fromJson(payload, HorseListResponse.class);
        System.out.println("Parsed horses: " + horseListResponse.horseList());
        updateHorseList(horseListResponse);
        break;
      case "createHorse":
        CreateHorseResponse createHorseResponse = gson.fromJson(payload, CreateHorseResponse.class);
          handleCreateHorseResponse(createHorseResponse);
        break;
      case "editHorse":
        break;
    }
  }

  @Override public void update(Object message)
  {

  }

  private void handleCreateHorseResponse(CreateHorseResponse createHorseResponse)
  {
    horseClient.getHorseList();
    if (createHorseResponse.horse() instanceof Horse horse){
      setSelectedHorse(horse);
    }
  }
}




