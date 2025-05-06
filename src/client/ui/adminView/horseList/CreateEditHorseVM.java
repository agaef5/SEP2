package client.ui.adminView.horseList;

import client.networking.SocketService;
import client.networking.horses.HorsesClient;
import client.ui.MessageListener;
import com.google.gson.Gson;
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
  private final BooleanProperty editButtonDisabled = new SimpleBooleanProperty();
  private final BooleanProperty removeButtonDisabled = new SimpleBooleanProperty();
  private final Gson gson;

  private Horse selectedHorse;
  private boolean creatingHorse;

  public CreateEditHorseVM(HorsesClient client,SocketService socketService) {
    this.horseClient = client;
    this.socketService = socketService;
    this.socketService.addListener(this);
    horseClient.getHorseList();
    this.gson = new Gson();
    creatingHorse = false;
  }

  public Property<String> horseNameProperty() { return horseName; }
  public IntegerProperty speedMinProperty() { return speedMin; }
  public IntegerProperty speedMaxProperty() { return speedMax; }
  public BooleanProperty getEditButtonDisabledProperty() { return editButtonDisabled;}
  public BooleanProperty getRemoveButtonDisableProperty() {return  removeButtonDisabled; }

  public ObservableList<Horse> getHorseList() {
    return horseList;
  }

  public void setSelectedHorse(Horse newVal) {
    this.selectedHorse = newVal;
    if (newVal != null) {
      horseName.set(newVal.getName());
      speedMin.set(newVal.getSpeedMin());
      speedMax.set(newVal.getSpeedMax());

      editButtonDisabled.set(false);
      removeButtonDisabled.set(false);
    }
  }

  public void setNull(){
    this.selectedHorse = null;
    horseName.set(null);
    speedMin.set(0);
    speedMax.set(0);

    editButtonDisabled.set(true);
    removeButtonDisabled.set(true);
  }

   public void addHorse() {
    if(!creatingHorse) return;

    Horse newHorse = new Horse(
        -1, // temporary ID
        horseName.get(),
        speedMin.get(),
        speedMax.get()
    );

    CreateHorseRequest createHorseRequest = new CreateHorseRequest(horseName.get(), speedMin.get(), speedMax.get());
    horseClient.createHorse(createHorseRequest);
    setReadMode();
  }

  //horse object is being directly manipulated, no DTO. We should change this
  public void updateHorse() {
    if (selectedHorse != null) {
      selectedHorse.setName(horseName.get());
      selectedHorse.setSpeedMin(speedMin.get());
      selectedHorse.setSpeedMax(speedMax.get());
      horseClient.updateHorse(selectedHorse);
    }
  }

  //horse object is being directly manipulated, no DTO. We should change this
  public void removeHorse() {
    if (selectedHorse != null) {
      horseClient.deleteHorse(selectedHorse);
      setNull();
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

      if(selectedHorse == null) setSelectedHorse(horseList.getFirst());
      System.out.println("List updated");
    });
  }

  public void setHorseCreationMode(){
    if(creatingHorse) addHorse();

    setNull();
    creatingHorse = true;
  }

public void setReadMode(){
    creatingHorse = false;
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
      case "updateHorse":
        Horse updatedHorse = gson.fromJson(payload, Horse.class);
        handleUpdateHorseResponse(updatedHorse);
        break;
      case "deleteHorse":
        String message = payload;
        handleRemoveHorseResponse(message);
        break;
    }
  }


  @Override public void update(Object message)
  {

  }

  private void handleCreateHorseResponse(CreateHorseResponse createHorseResponse)
  {
    horseClient.getHorseList();
    if (createHorseResponse.horse() != null){
      Horse newHorse = gson.fromJson(createHorseResponse.horse().toString(), Horse.class);
      setSelectedHorse(newHorse);
    }
  }

  private void handleUpdateHorseResponse(Horse updatedHorse)
  {
    horseClient.getHorseList();
    if (updatedHorse != null){
      setSelectedHorse(updatedHorse);
    }
  }

  private void handleRemoveHorseResponse(String message)
  {
    if(message.equals("success")){
      horseClient.getHorseList();
      setNull();
    }
  }

}




