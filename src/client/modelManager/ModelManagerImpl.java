package client.modelManager;

import client.networking.SocketService;
import client.networking.authentication.AuthenticationClient;
import client.networking.horses.HorsesClient;
import client.networking.race.RaceClient;
import client.ui.common.MessageListener;
import com.google.gson.Gson;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shared.DTO.HorseDTO;
import shared.DTO.RaceDTO;
import shared.DTO.RaceState;
import shared.DTO.RaceTrackDTO;
import shared.bet.CreateBetRequest;
import shared.horse.CreateHorseRequest;
import shared.horse.CreateHorseResponse;
import shared.horse.HorseListResponse;
import shared.loginRegister.LoginRequest;
import shared.loginRegister.LoginRespond;
import shared.loginRegister.RegisterRequest;
import shared.loginRegister.RegisterRespond;
import shared.race.*;
import shared.updates.OnRaceFinished;
import shared.updates.OnRaceStarted;

public class ModelManagerImpl implements ModelManager, MessageListener {

    private final AuthenticationClient authClient;
    private final RaceClient        raceClient;
    private final HorsesClient      horsesClient;
    private final SocketService     socketService;
    private final Gson gson = new Gson();

    // —— Authentication state ——
    private final BooleanProperty loginSuccess    = new SimpleBooleanProperty(false);
    private final StringProperty loginMessage    = new SimpleStringProperty("");
    private final BooleanProperty registerSuccess = new SimpleBooleanProperty(false);
    private final StringProperty  registerMessage = new SimpleStringProperty("");

    // —— Race data ——
    private final ObservableList<RaceTrackDTO> raceTracks   = FXCollections.observableArrayList();
    private final ObservableList<RaceDTO>      raceList     = FXCollections.observableArrayList();
    private final BooleanProperty              createRaceOk = new SimpleBooleanProperty(false);
    private final StringProperty               createRaceMsg= new SimpleStringProperty("");

    private final ObjectProperty<RaceDTO>      nextRace = new SimpleObjectProperty<RaceDTO>(null);
    private final ObjectProperty<RaceState>    raceState = new SimpleObjectProperty<>(null);
    private final ObservableList<HorseDTO>     raceRank = FXCollections.observableArrayList();

    // —— Horse data ——
    private final ObservableList<HorseDTO> horseList       = FXCollections.observableArrayList();
    private final BooleanProperty         createHorseOk   = new SimpleBooleanProperty(false);
    private final StringProperty          createHorseMsg  = new SimpleStringProperty("");
    private final BooleanProperty         updateHorseOk   = new SimpleBooleanProperty(false);
    private final StringProperty          updateHorseMsg  = new SimpleStringProperty("");
    private final BooleanProperty         deleteHorseOk   = new SimpleBooleanProperty(false);
    private final StringProperty          deleteHorseMsg  = new SimpleStringProperty("");

    public ModelManagerImpl(
            AuthenticationClient authClient,
            RaceClient raceClient,
            HorsesClient horsesClient,
            SocketService socketService
    ) {
        this.authClient   = authClient;
        this.raceClient   = raceClient;
        this.horsesClient = horsesClient;
        this.socketService= socketService;

        // subscribe once for all incoming socket messages
        this.socketService.addListener(this);
    }

    // —— Properties for VMs to bind to ——
    public BooleanProperty    loginSuccessProperty()    { return loginSuccess;  }
    public StringProperty     loginMessageProperty()    { return loginMessage; }
    public BooleanProperty    registerSuccessProperty() { return registerSuccess; }
    public StringProperty     registerMessageProperty() { return registerMessage; }

    public ObservableList<RaceTrackDTO> getRaceTracksList() { return raceTracks; }
    public ObservableList<RaceDTO>      getRaceList()       { return raceList;   }
    public ObjectProperty<RaceDTO>     nextRaceProperty()      { return nextRace; }
    public BooleanProperty    createRaceSuccessProperty() { return createRaceOk; }
    public StringProperty     createRaceMessageProperty() { return createRaceMsg; }
    public ObjectProperty<RaceState> getCurrentRaceState() {
        if (nextRace.get() != null)
            return null;
        return raceState;  }
    public ObservableList<HorseDTO> getRaceRank(){ return raceRank; };

    public ObservableList<HorseDTO>  getHorseList()            { return horseList;       }
    public BooleanProperty    createHorseSuccessProperty() { return createHorseOk;   }
    public StringProperty     createHorseMessageProperty() { return createHorseMsg;  }
    public BooleanProperty    updateHorseSuccessProperty() { return updateHorseOk;   }
    public StringProperty     updateHorseMessageProperty() { return updateHorseMsg;  }
    public BooleanProperty    deleteHorseSuccessProperty() { return deleteHorseOk;   }
    public StringProperty     deleteHorseMessageProperty() { return deleteHorseMsg;  }


    // —— Methods to call from ViewModels ——
    // Authentication
    public void loginUser(String identifier, String password){
        LoginRequest loginRequest = new LoginRequest(identifier, password);
        authClient.loginUser(loginRequest);
    }

    public void registerUser(String username, String email, String password) {
        RegisterRequest registerRequest = new RegisterRequest(username, email, password);
        authClient.registerUser(registerRequest);
    }

    // Race
    public void getRaceTracks(){
        raceClient.getRaceTracks(new GetRaceTracksRequest());
    }

    public void getAllRaces() {
        raceClient.getRaceList();
    }

    public void createRace(String name, RaceTrackDTO raceTrack, Integer capacity){
        CreateRaceRequest raceRequest = new CreateRaceRequest(name, raceTrack,capacity);
        raceClient.createRace(raceRequest);
    }

    @Override
    public void createBet(String username, HorseDTO horseDTO, int amount) {
    }

    @Override
    public void getBetList(String username) {

    }


    // Horse
    public void getAllHorses(){
        horsesClient.getHorseList();
    }

    public void createHorse(String name, int speedMin, int speedMax){
        CreateHorseRequest request = new CreateHorseRequest(name, speedMin, speedMax);
        horsesClient.createHorse(request);
    }

    public void updateHorse(HorseDTO horse){
        horsesClient.updateHorse(horse);
    }

    public void deleteHorse(HorseDTO horse){
        horsesClient.deleteHorse(horse);
    }


    // —— Method called from SocketService - MessageListener "update" implementation ——
    @Override
    public void update(String type, String payload){
        switch (type) {
            case "login":            handleLogin(payload);          break;
            case "register":         handleRegister(payload);       break;
            case "getRaceTracks":    handleGetRaceTracks(payload);  break;
            case "getRaceList":      handleGetRaceList(payload);    break;
            case "createRace":       handleCreateRace(payload);     break;
            case "horseMoveUpdate":  handleHorseMove(payload);      break;
            case "onHorseFinished":  handleOnHorseFinished(payload);break;
            case "onRaceStarted":    handleOnRaceStarted(payload);  break;
            case "onRaceFinished":   handleOnRaceFinished(payload); break;
            case "getHorseList":     handleGetHorseList(payload);   break;
            case "createHorse":      handleCreateHorse(payload);    break;
            case "updateHorse":      handleUpdateHorse(payload);    break;
            case "deleteHorse":      handleDeleteHorse(payload);    break;
        }
    }

    private void handleLogin(String payload){
        LoginRespond respond = gson.fromJson(payload, LoginRespond.class);
        if ("success".equals(respond.message())) {
            loginSuccess.set(true);
            loginMessage.set("");
        } else {
            loginSuccess.set(false);
            loginMessage.set(respond.payload().toString());
        }
    }

    private void handleRegister(String payload){
        RegisterRespond respond = gson.fromJson(payload, RegisterRespond.class);
        if ("success".equals(respond.message())) {
            registerSuccess.set(true);
            registerMessage.set("");
        } else {
            registerSuccess.set(false);
            registerMessage.set(respond.payload().toString());
        }
    }

    private void handleGetRaceTracks(String payload){
        GetRaceTrackResponse respond = gson.fromJson(payload, GetRaceTrackResponse.class);
        raceTracks.setAll(respond.raceTracks());
    }

    private void handleGetRaceList(String payload){
        GetRaceListResponse respond = gson.fromJson(payload, GetRaceListResponse.class);
        raceList.setAll(respond.races());
        if (!raceList.isEmpty()) {
            nextRace.set(raceList.get(0));
        } else {
            nextRace.set(null);
        }
    }

    private void handleCreateRace(String payload){
        RaceResponse respond = gson.fromJson(payload, RaceResponse.class);
        if (respond.race() != null) {
            createRaceOk.set(true);
            createRaceMsg.set("");
            getAllRaces();
        } else {
            createRaceOk.set(false);
            createRaceMsg.set("Failed to create race");
        }
    }

    private void handleOnRaceFinished(String payload) {
        OnRaceFinished respond = gson.fromJson(payload, OnRaceFinished.class);
//        TODO: check if its correct
        if(respond.raceName().equals(nextRace.get().name()))
            raceState.set(RaceState.FINISHED);
    }

    private void handleOnRaceStarted(String payload) {
        OnRaceStarted respond = gson.fromJson(payload, OnRaceStarted.class);
//        TODO: check if its correct
        if(respond.raceName().equals(nextRace.get().name()))
            raceState.set(RaceState.IN_PROGRESS);
    }

    private void handleOnHorseFinished(String payload) {

    }

    private void handleHorseMove(String payload) {
    }

    private void handleGetHorseList(String payload){
        HorseListResponse respond = gson.fromJson(payload, HorseListResponse.class);
        horseList.setAll(respond.horseList());
    }

    private void handleCreateHorse(String payload){
        CreateHorseResponse respond = gson.fromJson(payload, CreateHorseResponse.class);
        if (respond.horse() != null) {
            createHorseOk.set(true);
            createHorseMsg.set("");
            getAllHorses();
        } else {
            createHorseOk.set(false);
            createHorseMsg.set("Failed to create horse");
        }
    }

    private void handleUpdateHorse(String payload){
        HorseDTO updated = gson.fromJson(payload, HorseDTO.class);
        if (updated != null) {
            updateHorseOk.set(true);
            updateHorseMsg.set("");
            getAllHorses();
        } else {
            updateHorseOk.set(false);
            updateHorseMsg.set("Failed to update horse");
        }
    }

    private void handleDeleteHorse(String payload){
        if ("success".equals(payload)) {
            deleteHorseOk.set(true);
            deleteHorseMsg.set("");
            getAllHorses();
        } else {
            deleteHorseOk.set(false);
            deleteHorseMsg.set(payload);
        }
    }
}
