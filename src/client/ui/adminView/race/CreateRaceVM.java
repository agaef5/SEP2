package client.ui.adminView.race;

import client.networking.SocketService;
import client.networking.race.RaceClient;
import client.ui.MessageListener;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import server.model.Horse;
import server.model.RaceTrack;
import shared.CreateRaceRequest;
import shared.GetRaceListResponse;
import shared.GetRaceTrackResponse;
import shared.GetRaceTracksRequest;



public class CreateRaceVM implements MessageListener
{
  private final SocketService socketService;
  private final RaceClient raceClient;
  private final Gson gson;


  private StringProperty raceName = new SimpleStringProperty();
  private IntegerProperty horseCount = new SimpleIntegerProperty();
  private ObjectProperty<RaceTrack> selectedRaceTrack = new SimpleObjectProperty<>();
  private ObservableList<RaceTrack> availableRaceTracks = FXCollections.observableArrayList();

  public CreateRaceVM(RaceClient raceClient,SocketService socketService)
  {
    this.raceClient = raceClient;
    this.gson = new Gson();
    this.socketService = socketService;
    this.socketService.addListener(this);

    // Request race tracks when the ViewModel is initialized
    GetRaceTracksRequest request = new GetRaceTracksRequest();
    raceClient.getRaceTracks(request);
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



  public void createRace()
  {
    if (!isValid())
    {
      return;
    }
    CreateRaceRequest request = new CreateRaceRequest(
        raceName.get(),
        selectedRaceTrack.get(),
        horseCount.get()
    );

    raceClient.createRace(request);
  }

  @Override public void update(String type, String payload)
  {
    switch (type)
    {
      case "getRaceTracks":
        GetRaceTrackResponse trackResponse = gson.fromJson(payload, GetRaceTrackResponse.class);
        updateAvailableRaceTracks(trackResponse);
        break;
      case "getRaceList":
        GetRaceListResponse raceListResponse = gson.fromJson(payload,GetRaceListResponse.class);
        handleRaceList(raceListResponse);
        break;

        //TODO make like respond for created race
      //case "createRace":

    }

  }

  @Override public void update(Object message)
  {
  }

  private void updateAvailableRaceTracks(GetRaceTrackResponse response)
  {
    Platform.runLater(()->
        availableRaceTracks.setAll(response.raceTracks()));
  }

  private void handleRaceList(GetRaceListResponse response)
  {
    Platform.runLater(()->{
      System.out.println("Received race list: "+ response.races());
    });
  }

}



