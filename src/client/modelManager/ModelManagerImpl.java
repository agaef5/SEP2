package client.modelManager;

import client.networking.SocketService;
import client.networking.authentication.AuthenticationClient;
import client.networking.bet.BetClient;
import client.networking.horses.HorsesClient;
import client.networking.race.RaceClient;
import client.ui.util.ErrorHandler;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shared.DTO.*;
import shared.bet.CreateBetRequest;
import shared.bet.CreateBetResponse;
import shared.horse.CreateHorseRequest;
import shared.horse.CreateHorseResponse;
import shared.horse.HorseListResponse;
import shared.loginRegister.LoginRequest;
import shared.loginRegister.LoginRespond;
import shared.loginRegister.RegisterRequest;
import shared.loginRegister.RegisterRespond;
import shared.race.*;
import shared.updates.HorsePositionsUpdate;
import shared.updates.OnRaceFinished;
import shared.updates.OnRaceStarted;
import shared.user.UserRequest;
import shared.user.UserResponse;

import java.util.Arrays;
import java.util.List;

public class ModelManagerImpl implements ModelManager, MessageListener {

    private final AuthenticationClient authClient;
    private final RaceClient        raceClient;
    private final HorsesClient      horsesClient;
    private final BetClient         betClient;
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
    private final ObjectProperty<RaceDTO>      createdRace = new SimpleObjectProperty<>(null);

    private final BooleanProperty              raceStarted = new SimpleBooleanProperty(false);
    private final StringProperty               currentRaceName = new SimpleStringProperty("");
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

    // —— Bet data ——
    private final BooleanProperty betPlaced = new SimpleBooleanProperty(false);

    // —— Game data ——
    private final ObservableList<Integer> horsePositions = FXCollections.observableArrayList();

    // —— User data ——
    private UserDTO currentUser;
    private final IntegerProperty userBalance = new SimpleIntegerProperty(0);

    public ModelManagerImpl(
            AuthenticationClient authClient,
            RaceClient raceClient,
            HorsesClient horsesClient,
            BetClient betClient,
            SocketService socketService

    ) {
        this.authClient   = authClient;
        this.raceClient   = raceClient;
        this.horsesClient = horsesClient;
        this.socketService= socketService;
        this.betClient = betClient;


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
    public ObjectProperty<RaceDTO> getNextRace()      { return nextRace; }
    public BooleanProperty    createRaceSuccessProperty() { return createRaceOk; }
    public StringProperty     createRaceMessageProperty() { return createRaceMsg; }
    public ObjectProperty<RaceDTO> getCreatedRace() { return  createdRace;};

    public ObjectProperty<RaceState> getCurrentRaceState() {
        if (nextRace.get() != null)
            return null;
        return raceState;  }
    public ObservableList<HorseDTO> getRaceRank(){ return raceRank; };
    public BooleanProperty raceStartedProperty() { return raceStarted; }
    public StringProperty currentRaceNameProperty() { return currentRaceName; }

    public BooleanProperty betPlacedProperty() { return betPlaced; }
    public ObservableList<Integer> getHorsePositions() { return horsePositions; }

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
    public void createBet(HorseDTO horseDTO, int amount) {
        // Validation check
        if (!validateBet(horseDTO, amount)) {
            System.err.println("Bet validation failed");
            return;
        }

        // Get username from currentUser
        if (currentUser == null) {
            System.err.println("No current user for betting");
            return;
        }

        // Create request
        CreateBetRequest request = new CreateBetRequest(currentUser.username(), horseDTO, amount);

        // Send to server
        betClient.createBet(request);
    }

    // Horse
    public void getAllHorses(){
        horsesClient.getHorseList();
    }

    public void createHorse(String name, int speedMin, int speedMax){
        CreateHorseRequest request = new CreateHorseRequest(name, speedMin, speedMax);
        horsesClient.createHorse(request);
    }
    @Override
    public void updateHorse(int id,String horseName,int speedMin,int speedMax ){
        HorseDTO horse = new HorseDTO(id,horseName,speedMin,speedMax);
        horsesClient.updateHorse(horse);
    }

    public void deleteHorse(HorseDTO horse){
        horsesClient.deleteHorse(horse);
    }

    public IntegerProperty getUserBalance (){
        return userBalance;
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
            case "getUser":          handleGetUser(payload);          break;
            case "createBet":        handleCreateBet(payload);       break;
        }
    }

    private void handleLogin(String payload){
        LoginRespond respond = gson.fromJson(payload, LoginRespond.class);
        Platform.runLater(() -> {
            if ("success".equals(respond.message()) && !payload.isEmpty()) {
                UserDTO userDTO = gson.fromJson(gson.toJson(respond.payload()), UserDTO.class);
                setCurrentUser(userDTO);

                loginSuccess.set(true);
                loginMessage.set("");
            } else {
                loginSuccess.set(false);
                loginMessage.set(respond.payload().toString());
            }
        });
    }

    private void handleRegister(String payload){
        RegisterRespond respond = gson.fromJson(payload, RegisterRespond.class);
        Platform.runLater(() -> {
            if ("success".equals(respond.message())) {
                UserDTO userDTO = gson.fromJson(gson.toJson(respond.payload()), UserDTO.class);
                setCurrentUser(userDTO);

                registerSuccess.set(true);
                registerMessage.set("");
            } else {
                registerSuccess.set(false);
                registerMessage.set(respond.payload().toString());
            }
        });

    }

    private void handleGetRaceTracks(String payload){
        GetRaceTrackResponse respond = gson.fromJson(payload, GetRaceTrackResponse.class);
        Platform.runLater(() -> {
        raceTracks.setAll(respond.raceTracks());
        });
    }

    private void handleGetRaceList(String payload){
        GetRaceListResponse respond = gson.fromJson(payload, GetRaceListResponse.class);
        Platform.runLater(() -> {
            raceList.setAll(respond.races());

            if (!raceList.isEmpty()) {
                nextRace.set(raceList.get(0));
            } else {
                nextRace.set(null);
            }
        });
    }

    private void handleCreateRace(String payload){
        RaceResponse respond = gson.fromJson(payload, RaceResponse.class);

        Platform.runLater(() -> {
            if (respond.race() != null) {
                RaceDTO raceDTO = gson.fromJson(gson.toJson(respond.race()), RaceDTO.class);
                createdRace.set(raceDTO);

                createRaceOk.set(true);
                createRaceMsg.set("");
                getAllRaces();
            } else {
                createRaceOk.set(false);
                createRaceMsg.set("Failed to create race");
            }
        });
    }

    private void handleOnRaceStarted(String payload) {
        OnRaceStarted raceStarted = gson.fromJson(payload, OnRaceStarted.class);
        Platform.runLater(() -> {
            if (raceStarted.raceName().equals(nextRace.get().name())) {
                this.currentRaceName.set(raceStarted.raceName());
                this.raceStarted.set(true);
                raceState.set(RaceState.IN_PROGRESS);
            }
        });
    }

    private void handleOnRaceFinished(String payload) {
        Platform.runLater(() -> {
        raceStarted.set(false);
        currentRaceName.set("");

        OnRaceFinished respond = gson.fromJson(payload, OnRaceFinished.class);

        if(respond.raceName().equals(nextRace.get().name()))
            raceState.set(RaceState.FINISHED);
        });
    }


    private void handleOnHorseFinished(String payload)
    {

    }

    private void handleHorseMove(String payload) {
        // Parse horse positions from payload
        HorsePositionsUpdate update = gson.fromJson(payload, HorsePositionsUpdate.class);

        // Update property
        Platform.runLater(() -> {
            horsePositions.setAll(update.positions());
        });
    }

    private void handleGetHorseList(String payload){
        HorseListResponse respond = gson.fromJson(payload, HorseListResponse.class);
        Platform.runLater(() -> {
        horseList.setAll(respond.horseList());
        });
    }

    private void handleCreateHorse(String payload){
        CreateHorseResponse respond = gson.fromJson(payload, CreateHorseResponse.class);
        Platform.runLater(()->{if (respond.horse() != null) {
            createHorseOk.set(true);
            createHorseMsg.set("");
            getAllHorses();
        } else {
            createHorseOk.set(false);
            createHorseMsg.set("Failed to create horse");
        }});

    }

    private void handleUpdateHorse(String payload){
        HorseDTO updated = gson.fromJson(payload, HorseDTO.class);
        Platform.runLater(()->{if (updated != null) {
            updateHorseOk.set(true);
            updateHorseMsg.set("");
            getAllHorses();
        } else {
            updateHorseOk.set(false);
            updateHorseMsg.set("Failed to update horse");
        }});

    }

    private void handleDeleteHorse(String payload){
        Platform.runLater(()->{if ("success".equals(payload)) {
            deleteHorseOk.set(true);
            deleteHorseMsg.set("");
            getAllHorses();
        } else {
            deleteHorseOk.set(false);
            deleteHorseMsg.set(payload);
        }});

    }

    private void handleCreateBet(String payload)
    {
        CreateBetResponse response = gson.fromJson(payload, CreateBetResponse.class);
        Platform.runLater(() ->{
            if (response.BetDTO() != null)
            {
                // Bet was successful
                loadCurrentUser();
                betPlaced.set(true);
            }
            else
            {
                // Bet failed
                betPlaced.set(false);
            }
        } );

    }

    private void handleGetUser(String payload)
    {
        UserResponse userResponse = gson.fromJson(payload, UserResponse.class);

        if("succes".equals(userResponse.message()))
        {
            UserDTO userDTO = gson.fromJson(gson.toJson(userResponse.payload()), UserDTO.class);
            Platform.runLater(() -> {
                userBalance.set(userDTO.balance());
                this.currentUser = userDTO;
            });
        }
        else
        {
            ErrorHandler.handleError(new Exception("Cannot get user"), this.getClass().getName());
        }
    }

    public void loadCurrentUser()
    {
        if (currentUser!=null)
        {
            UserRequest request = new UserRequest(currentUser.username());
            authClient.getUser(request);
        }
    }

public UserDTO getCurrentUser(){
        if(currentUser == null){
            loadCurrentUser();
        }
        return currentUser;
    }

        public void setCurrentUser(UserDTO userDTO)
    {
        Platform.runLater(() -> {
                    this.currentUser = userDTO;
                    userBalance.set(userDTO.balance());
                });
        loadCurrentUser();
    };

    public boolean validateBet(HorseDTO horse, int amount) {
        // Check if horse is selected
        if (horse == null) return false;

        // Check if amount is positive
        if (amount <= 0) return false;

        // Check if user has enough balance
        if (amount > userBalance.get()) return false;

//        if(!raceState.get().equals(RaceState.NOT_STARTED)) return false;

        return true;
    }
}
