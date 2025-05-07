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
  import server.model.Race;
  import server.model.RaceTrack;
  import shared.*;

  public class CreateRaceVM implements MessageListener
  {
    private final SocketService socketService;
    private final RaceClient raceClient;
    private final Gson gson;
    private StringProperty raceName = new SimpleStringProperty();
    private IntegerProperty horseCount = new SimpleIntegerProperty();
    private ObjectProperty<RaceTrack> selectedRaceTrack = new SimpleObjectProperty<>();
    private ObservableList<RaceTrack> availableRaceTracks = FXCollections.observableArrayList();
    private ObservableList<Race> raceQueue = FXCollections.observableArrayList();

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

    public ObservableList<Race> getRaceQueue(){
      return raceQueue;
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

      //clearing input fiels after creating
      raceName.set("");
      horseCount.set(0);
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
        case "createRace":
          CreateRaceResponse createRaceResponse = gson.fromJson(payload, CreateRaceResponse.class);
          handleCreateRaceResponse (createRaceResponse);
          raceClient.getRaceList();
          if (createRaceResponse.Race() !=null){
            Race newRace = gson.fromJson(createRaceResponse.Race().toString(), Race.class);
            setSelectedRace(newRace);
         }
       }
    }

    private void setSelectedRace(Race newRace)
    {
      //do we need this?
    }

    private void handleCreateRaceResponse(CreateRaceResponse createRaceResponse)
    {
      Platform.runLater(() -> {
        // Handle the create race response
        if (createRaceResponse.Race() != null) {
          System.out.println("Race created successfully");
        } else {
          System.out.println("Failed to create race");
        }
      });
    }

       private void updateAvailableRaceTracks(GetRaceTrackResponse response)
    {
      Platform.runLater(()->
          availableRaceTracks.setAll(response.raceTracks()));
    }

    private void handleRaceList(GetRaceListResponse response)
    {
      Platform.runLater(() -> {
        // Update the race queue with the received races
        raceQueue.clear();
        if (response.races() != null) {
          raceQueue.addAll(response.races());
        }
        System.out.println("Race queue updated with " + raceQueue.size() + " races");
      });
    }


  }



