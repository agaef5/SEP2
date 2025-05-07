package client.ui.userView;

import client.networking.SocketService;
import client.networking.horses.HorsesClient;
import client.ui.MessageListener;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.model.Horse;
import shared.CreateHorseResponse;
import shared.HorseListResponse;
import shared.Respond;

import java.util.ArrayList;

public class HorseListVM implements MessageListener
{
  private HorsesClient horseClient;
  private final SocketService socketService;
  private ObservableList<Horse> horseList = FXCollections.observableArrayList();

  private final StringProperty horseName = new SimpleStringProperty();
  private final IntegerProperty speedMin = new SimpleIntegerProperty();
  private final IntegerProperty speedMax = new SimpleIntegerProperty();
  private final Gson gson;

  private Horse selectedHorse;

  public HorseListVM(HorsesClient horsesClient, SocketService socketService)
  {
    this.horseClient = horsesClient;
    this.socketService = socketService;
    this.socketService.addListener(this);
    horseClient.getHorseList();
    this.gson = new Gson();
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

  @Override
  public void update(String type, String payload){
    System.out.println("Message received: " + type);
    switch (type) {
      case "getHorseList":
        HorseListResponse horseListResponse = gson.fromJson(payload, HorseListResponse.class);
        System.out.println("Parsed horses: " + horseListResponse.horseList());
        updateHorseList(horseListResponse);
        break;
    }
  }
}
