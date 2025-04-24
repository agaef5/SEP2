package client.ui.racerList.adminView;

import client.networking.SocketService;
import client.networking.racers.RacersClient;
import client.ui.MessageListener;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.model.Horse;
import server.model.Racer;
import shared.CreateRacerRequest;
import shared.Respond;
import shared.RacerListResponse;

import java.util.ArrayList;
import java.util.List;

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
    this.gson = new Gson();
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


//  public void updateRacerList(RacerListResponse racerListResponse)
//  {
//    List<Racer> racerList = racerListResponse.racerList();
//    for (Racer racer: racerList)
//    {
//      getRacerList().add(racer);
//    }
//
//  }

  @Override
  public void update(String message) {
    // TODO Ensure the server consistently uses a single response format.
    //
    // 1. Refactor all server endpoints to wrap their output in the Respond class:
    //
    // 2. In each handler, replace direct payload returns with:
    //       return new Respond(payload);
    //
    //
    // By standardizing on Respond, the client can reliably deserialize every message with:
    //     Respond resp = gson.fromJson(json, Respond.class);
    //     List<Racer> racers = resp.getRacerList();

    // now it doesnt work bc the respond we are getting is not in format of "Respond"




    System.out.println("Received>> "+message);
    Respond respond = gson.fromJson(message, Respond.class);
      if (respond.payload() instanceof RacerListResponse racerListResponse) {
        Platform.runLater(() -> {
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
    }
  }




