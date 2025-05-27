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
    private final IntegerProperty userBalance = new SimpleIntegerProperty(0);
    // —— User data ——
    private UserDTO currentUser;

    /**
     * Constructs a new instance of ModelManagerImpl.
     * Initializes the networking clients used for authentication, race, horse, and bet operations.
     * Also registers this instance as a listener to the SocketService to receive real-time updates.
     *
     * @param authClient      the client responsible for user login and registration
     * @param raceClient      the client used to manage race-related server communication
     * @param horsesClient    the client used to manage horse-related server communication
     * @param betClient       the client responsible for creating and sending bet requests
     * @param socketService   the socket service used for sending requests and receiving server updates
     */
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
        this.betClient    = betClient;

        // subscribe once for all incoming socket messages
        this.socketService.addListener(this);
    }


    /**
     * Properties for ViewModels to bind to.
     * These provide observable state for authentication, race, horse, betting, and game status.
     */

    /** @return property indicating login success */
    public BooleanProperty loginSuccessProperty() { return loginSuccess; }

    /** @return property holding login result message */
    public StringProperty loginMessageProperty() { return loginMessage; }

    /** @return property indicating registration success */
    public BooleanProperty registerSuccessProperty() { return registerSuccess; }

    /** @return property holding registration result message */
    public StringProperty registerMessageProperty() { return registerMessage; }

    /** @return observable list of race tracks */
    public ObservableList<RaceTrackDTO> getRaceTracksList() { return raceTracks; }

    /** @return observable list of races */
    public ObservableList<RaceDTO> getRaceList() { return raceList; }

    /** @return property holding the next upcoming race */
    public ObjectProperty<RaceDTO> getNextRace() { return nextRace; }

    /** @return property indicating race creation success */
    public BooleanProperty createRaceSuccessProperty() { return createRaceOk; }

    /** @return property holding race creation result message */
    public StringProperty createRaceMessageProperty() { return createRaceMsg; }

    /** @return property containing the most recently created race */
    public ObjectProperty<RaceDTO> getCreatedRace() { return createdRace; }

    /**
     * Returns the current race state if no upcoming race is set.
     * @return race state or null if nextRace is set
     */
    public ObjectProperty<RaceState> getCurrentRaceState() {
        if (nextRace.get() != null)
            return null;
        return raceState;
    }

    /** @return observable list of horse rankings in the race */
    public ObservableList<HorseDTO> getRaceRank() { return raceRank; }

    /** @return property indicating whether a race has started */
    public BooleanProperty raceStartedProperty() { return raceStarted; }

    /** @return property holding the name of the current race */
    public StringProperty currentRaceNameProperty() { return currentRaceName; }

    /** @return property indicating whether a bet was successfully placed */
    public BooleanProperty betPlacedProperty() { return betPlaced; }

    /** @return observable list of current horse positions during race */
    public ObservableList<Integer> getHorsePositions() { return horsePositions; }

    /** @return observable list of horses in the system */
    public ObservableList<HorseDTO> getHorseList() { return horseList; }

    /** @return property indicating horse creation success */
    public BooleanProperty createHorseSuccessProperty() { return createHorseOk; }

    /** @return property holding horse creation result message */
    public StringProperty createHorseMessageProperty() { return createHorseMsg; }

    /** @return property indicating horse update success */
    public BooleanProperty updateHorseSuccessProperty() { return updateHorseOk; }

    /** @return property holding horse update result message */
    public StringProperty updateHorseMessageProperty() { return updateHorseMsg; }

    /** @return property indicating horse deletion success */
    public BooleanProperty deleteHorseSuccessProperty() { return deleteHorseOk; }

    /** @return property holding horse deletion result message */
    public StringProperty deleteHorseMessageProperty() { return deleteHorseMsg; }

    /**
     * Sends a login request using the provided credentials.
     *
     * @param identifier the user's username or email
     * @param password the user's password
     */
    public void loginUser(String identifier, String password){
        LoginRequest loginRequest = new LoginRequest(identifier, password);
        authClient.loginUser(loginRequest);
    }

    /**
     * Sends a registration request with the provided information.
     *
     * @param username the desired username
     * @param email the user's email address
     * @param password the desired password
     */
    public void registerUser(String username, String email, String password) {
        RegisterRequest registerRequest = new RegisterRequest(username, email, password);
        authClient.registerUser(registerRequest);
    }

    /**
     * Requests all available race tracks from the server.
     */
    public void getRaceTracks(){
        raceClient.getRaceTracks(new GetRaceTracksRequest());
    }

    /**
     * Requests a list of all races from the server.
     */
    public void getAllRaces() {
        raceClient.getRaceList();
    }

    /**
     * Sends a request to create a new race.
     *
     * @param name the name of the race
     * @param raceTrack the selected race track
     * @param capacity number of horses allowed in the race
     */
    public void createRace(String name, RaceTrackDTO raceTrack, Integer capacity){
        CreateRaceRequest raceRequest = new CreateRaceRequest(name, raceTrack, capacity);
        raceClient.createRace(raceRequest);
    }

    /**
     * Places a bet on the specified horse for a given amount.
     * Validates the bet before sending it to the server.
     *
     * @param horseDTO the horse to bet on
     * @param amount the amount of money to bet
     */
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


    /**
     * Requests a list of all horses from the server.
     */
    public void getAllHorses() {
        horsesClient.getHorseList();
    }

    /**
     * Sends a request to create a new horse with the given attributes.
     *
     * @param name the horse's name
     * @param speedMin the minimum speed of the horse
     * @param speedMax the maximum speed of the horse
     */
    public void createHorse(String name, int speedMin, int speedMax) {
        CreateHorseRequest request = new CreateHorseRequest(name, speedMin, speedMax);
        horsesClient.createHorse(request);
    }

    /**
     * Sends a request to update an existing horse with new values.
     *
     * @param id the ID of the horse to update
     * @param horseName the new name for the horse
     * @param speedMin the new minimum speed
     * @param speedMax the new maximum speed
     */
    @Override
    public void updateHorse(int id, String horseName, int speedMin, int speedMax) {
        HorseDTO horse = new HorseDTO(id, horseName, speedMin, speedMax);
        horsesClient.updateHorse(horse);
    }

    /**
     * Sends a request to delete the specified horse from the server.
     *
     * @param horse the horse to delete
     */
    public void deleteHorse(HorseDTO horse) {
        horsesClient.deleteHorse(horse);
    }

    /**
     * Returns the user's current balance as an observable property.
     *
     * @return the user balance property
     */
    public IntegerProperty getUserBalance() {
        return userBalance;
    }

    /**
     * Handles incoming server messages via the SocketService.
     * Dispatches each message type to the appropriate handler method.
     *
     * @param type the message type key (e.g. \"login\", \"createRace\")
     * @param payload the JSON-encoded message data
     */
    @Override
    public void update(String type, String payload) {
        switch (type) {
            case "login":            handleLogin(payload);           break;
            case "register":         handleRegister(payload);        break;
            case "getRaceTracks":    handleGetRaceTracks(payload);   break;
            case "getRaceList":      handleGetRaceList(payload);     break;
            case "createRace":       handleCreateRace(payload);      break;
            case "horseMoveUpdate":  handleHorseMove(payload);       break;
            case "onHorseFinished":  handleOnHorseFinished(payload); break;
            case "onRaceStarted":    handleOnRaceStarted(payload);   break;
            case "onRaceFinished":   handleOnRaceFinished(payload);  break;
            case "getHorseList":     handleGetHorseList(payload);    break;
            case "createHorse":      handleCreateHorse(payload);     break;
            case "updateHorse":      handleUpdateHorse(payload);     break;
            case "deleteHorse":      handleDeleteHorse(payload);     break;
            case "getUser":          handleGetUser(payload);         break;
            case "createBet":        handleCreateBet(payload);       break;
        }
    }


    /**
     * Handles the login response from the server.
     * If successful, sets the current user and updates login status properties.
     *
     * @param payload the JSON payload containing the login response
     */
    private void handleLogin(String payload) {
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

    /**
     * Handles the register response from the server.
     * If successful, sets the current user and updates registration status properties.
     *
     * @param payload the JSON payload containing the registration response
     */
    private void handleRegister(String payload) {
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

    /**
     * Handles the response containing the list of race tracks.
     * Updates the observable list of race tracks.
     *
     * @param payload the JSON payload with race track data
     */
    private void handleGetRaceTracks(String payload) {
        GetRaceTrackResponse respond = gson.fromJson(payload, GetRaceTrackResponse.class);
        Platform.runLater(() -> {
            raceTracks.setAll(respond.raceTracks());
        });
    }


    /**
     * Handles the response from the server containing a list of races.
     * Updates the local race list and sets the next race if available.
     *
     * @param payload the JSON payload containing the race list
     */
    private void handleGetRaceList(String payload) {
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

    /**
     * Handles the response after attempting to create a race.
     * Updates the created race and its status, and refreshes the race list if successful.
     *
     * @param payload the JSON payload containing the creation result
     */
    private void handleCreateRace(String payload) {
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

    /**
     * Handles the event when a race has started.
     * Updates the current race name and state to IN_PROGRESS.
     *
     * @param payload the JSON payload containing race start info
     */
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

    /**
     * Handles the event when a race has finished.
     * Resets the current race name and sets the race state to FINISHED.
     *
     * @param payload the JSON payload containing race finish info
     */
    private void handleOnRaceFinished(String payload) {
        Platform.runLater(() -> {
            raceStarted.set(false);
            currentRaceName.set("");

            OnRaceFinished respond = gson.fromJson(payload, OnRaceFinished.class);

            if (respond.raceName().equals(nextRace.get().name())) {
                raceState.set(RaceState.FINISHED);
            }
        });
    }



    /**
     * Placeholder for handling a horse finish event.
     * This method is currently not implemented.
     *
     * @param payload the JSON payload (not used yet)
     */
    private void handleOnHorseFinished(String payload) {
        // TODO: Implement this when horse finish logic is defined
    }

    /**
     * Handles updates to horse positions during a race.
     * Parses the update and refreshes the horsePositions list.
     *
     * @param payload the JSON payload containing updated horse positions
     */
    private void handleHorseMove(String payload) {
        // Parse horse positions from payload
        HorsePositionsUpdate update = gson.fromJson(payload, HorsePositionsUpdate.class);

        // Update property
        Platform.runLater(() -> {
            horsePositions.setAll(update.positions());
        });
    }

    /**
     * Handles the response containing the list of horses.
     * Updates the local horse list observable.
     *
     * @param payload the JSON payload with horse data
     */
    private void handleGetHorseList(String payload) {
        HorseListResponse respond = gson.fromJson(payload, HorseListResponse.class);
        Platform.runLater(() -> {
            horseList.setAll(respond.horseList());
        });
    }

    /**
     * Handles the response after creating a new horse.
     * Updates creation success flags and refreshes the horse list if successful.
     *
     * @param payload the JSON payload containing the creation result
     */
    private void handleCreateHorse(String payload) {
        CreateHorseResponse respond = gson.fromJson(payload, CreateHorseResponse.class);
        Platform.runLater(() -> {
            if (respond.horse() != null) {
                createHorseOk.set(true);
                createHorseMsg.set("");
                getAllHorses();
            } else {
                createHorseOk.set(false);
                createHorseMsg.set("Failed to create horse");
            }
        });
    }

    /**
     * Handles the response after updating a horse.
     * Sets success flag and message, and refreshes the horse list if successful.
     *
     * @param payload the JSON payload containing updated horse data
     */
    private void handleUpdateHorse(String payload) {
        HorseDTO updated = gson.fromJson(payload, HorseDTO.class);
        Platform.runLater(() -> {
            if (updated != null) {
                updateHorseOk.set(true);
                updateHorseMsg.set("");
                getAllHorses();
            } else {
                updateHorseOk.set(false);
                updateHorseMsg.set("Failed to update horse");
            }
        });
    }

    /**
     * Handles the response after deleting a horse.
     * Sets success flag and message, and refreshes the horse list if deletion succeeded.
     *
     * @param payload the JSON payload containing a success message or error
     */
    private void handleDeleteHorse(String payload) {
        Platform.runLater(() -> {
            if ("success".equals(payload)) {
                deleteHorseOk.set(true);
                deleteHorseMsg.set("");
                getAllHorses();
            } else {
                deleteHorseOk.set(false);
                deleteHorseMsg.set(payload);
            }
        });
    }

    /**
     * Handles the server response after a bet is placed.
     * Updates betPlaced flag and reloads the current user's data if successful.
     *
     * @param payload the JSON payload containing the bet result
     */
    private void handleCreateBet(String payload) {
        CreateBetResponse response = gson.fromJson(payload, CreateBetResponse.class);
        Platform.runLater(() -> {
            if (response.BetDTO() != null) {
                // Bet was successful
                loadCurrentUser();
                betPlaced.set(true);
            } else {
                // Bet failed
                betPlaced.set(false);
            }
        });
    }

    /**
     * Handles the server response for a user data request.
     * Updates the current user's balance if successful; otherwise logs an error.
     *
     * @param payload the JSON payload containing the user data or error
     */
    private void handleGetUser(String payload) {
        UserResponse userResponse = gson.fromJson(payload, UserResponse.class);

        if ("succes".equals(userResponse.message())) {
            UserDTO userDTO = gson.fromJson(gson.toJson(userResponse.payload()), UserDTO.class);
            Platform.runLater(() -> {
                userBalance.set(userDTO.balance());
                this.currentUser = userDTO;
            });
        } else {
            ErrorHandler.handleError(new Exception("Cannot get user"), this.getClass().getName());
        }
    }

    /**
     * Sends a request to reload the current user's information from the server.
     * Only sends the request if a user is already logged in.
     */
    public void loadCurrentUser() {
        if (currentUser != null) {
            UserRequest request = new UserRequest(currentUser.username());
            authClient.getUser(request);
        }
    }

    /**
     * Returns the currently logged-in user.
     * If not already loaded, it triggers a load from the server first.
     *
     * @return the current user DTO
     */
    public UserDTO getCurrentUser() {
        if (currentUser == null) {
            loadCurrentUser();
        }
        return currentUser;
    }

    /**
     * Sets the current user and updates the user balance.
     * Triggers a reload of the user data from the server after setting.
     *
     * @param userDTO the user to set as currently logged in
     */
    public void setCurrentUser(UserDTO userDTO) {
        Platform.runLater(() -> {
            this.currentUser = userDTO;
            userBalance.set(userDTO.balance());
        });
        loadCurrentUser();
    }

    /**
     * Validates a bet before sending it to the server.
     * Ensures that a horse is selected, the amount is positive, and the user has enough balance.
     *
     * @param horse the horse being bet on
     * @param amount the amount of money to bet
     * @return true if the bet is valid, false otherwise
     */
    public boolean validateBet(HorseDTO horse, int amount) {
        // Check if horse is selected
        if (horse == null) return false;

        // Check if amount is positive
        if (amount <= 0) return false;

        // Check if user has enough balance
        if (amount > userBalance.get()) return false;

        // if(!raceState.get().equals(RaceState.NOT_STARTED)) return false;

        return true;
    }
}

