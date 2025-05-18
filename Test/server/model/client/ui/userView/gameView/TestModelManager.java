package client.ui.userView.gameView;

import client.modelManager.ModelManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Duration;
import shared.DTO.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestModelManager implements ModelManager {
    // Horse positions (animated)
    private final ObservableList<Integer> horsePositions = FXCollections.observableArrayList(0, 0, 0);

    // Race properties
    private final BooleanProperty raceStarted = new SimpleBooleanProperty(true);
    private final StringProperty currentRaceName = new SimpleStringProperty("Test Race");
    private final ObjectProperty<RaceDTO> nextRace = new SimpleObjectProperty<>();
    private final ObjectProperty<RaceState> currentRaceState = new SimpleObjectProperty<>(RaceState.IN_PROGRESS);
    private final ObservableList<RaceDTO> raceList = FXCollections.observableArrayList();
    private final ObservableList<HorseDTO> raceRank = FXCollections.observableArrayList();

    // Horse properties
    private final ObservableList<HorseDTO> horseList = FXCollections.observableArrayList();

    // User properties
    private final IntegerProperty userBalance = new SimpleIntegerProperty(1000);

    // All other properties as stubs
    private final BooleanProperty loginSuccess = new SimpleBooleanProperty();
    private final StringProperty loginMessage = new SimpleStringProperty();
    private final BooleanProperty registerSuccess = new SimpleBooleanProperty();
    private final StringProperty registerMessage = new SimpleStringProperty();
    private final ObservableList<RaceTrackDTO> raceTracksList = FXCollections.observableArrayList();
    private final BooleanProperty createRaceSuccess = new SimpleBooleanProperty();
    private final StringProperty createRaceMessage = new SimpleStringProperty();
    private final BooleanProperty createHorseSuccess = new SimpleBooleanProperty();
    private final StringProperty createHorseMessage = new SimpleStringProperty();
    private final BooleanProperty updateHorseSuccess = new SimpleBooleanProperty();
    private final StringProperty updateHorseMessage = new SimpleStringProperty();
    private final BooleanProperty deleteHorseSuccess = new SimpleBooleanProperty();
    private final StringProperty deleteHorseMessage = new SimpleStringProperty();
    private final BooleanProperty betPlaced = new SimpleBooleanProperty();

    public TestModelManager() {
        // Animate horse positions every 2 seconds
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), e -> {
            Random random = new Random();
            List<Integer> newPositions = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                int currentPos = horsePositions.get(i);
                int newPos = Math.min(1000, currentPos + random.nextInt(100) + 20);
                newPositions.add(newPos);
            }
            Platform.runLater(() -> horsePositions.setAll(newPositions));
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    // —— Properties for VMs to bind to ——
    @Override public BooleanProperty loginSuccessProperty() { return loginSuccess; }
    @Override public StringProperty loginMessageProperty() { return loginMessage; }
    @Override public BooleanProperty registerSuccessProperty() { return registerSuccess; }
    @Override public StringProperty registerMessageProperty() { return registerMessage; }
    @Override public ObservableList<RaceTrackDTO> getRaceTracksList() { return raceTracksList; }
    @Override public ObservableList<RaceDTO> getRaceList() { return raceList; }
    @Override public ObjectProperty<RaceDTO> nextRaceProperty() { return nextRace; }
    @Override public BooleanProperty createRaceSuccessProperty() { return createRaceSuccess; }
    @Override public StringProperty createRaceMessageProperty() { return createRaceMessage; }
    @Override public ObjectProperty<RaceState> getCurrentRaceState() { return currentRaceState; }
    @Override public ObservableList<HorseDTO> getRaceRank() { return raceRank; }
    @Override public BooleanProperty raceStartedProperty() { return raceStarted; }
    @Override public StringProperty currentRaceNameProperty() { return currentRaceName; }
    @Override public ObservableList<HorseDTO> getHorseList() { return horseList; }
    @Override public BooleanProperty createHorseSuccessProperty() { return createHorseSuccess; }
    @Override public StringProperty createHorseMessageProperty() { return createHorseMessage; }
    @Override public BooleanProperty updateHorseSuccessProperty() { return updateHorseSuccess; }
    @Override public StringProperty updateHorseMessageProperty() { return updateHorseMessage; }
    @Override public BooleanProperty deleteHorseSuccessProperty() { return deleteHorseSuccess; }
    @Override public StringProperty deleteHorseMessageProperty() { return deleteHorseMessage; }
    @Override public BooleanProperty betPlacedProperty() { return betPlaced; }
    @Override public IntegerProperty getUserBalance() { return userBalance; }
    @Override public ObservableList<Integer> getHorsePositions() { return horsePositions; }

    // —— Methods (all stubs except what's needed for test) ——
    @Override public void loginUser(String username, String password) {}
    @Override public void registerUser(String username, String email, String password) {}
    @Override public void getRaceTracks() {}
    @Override public void getAllRaces() {}
    @Override public void createRace(String name, RaceTrackDTO raceTrack, Integer capacity) {}
    @Override public void createBet(HorseDTO horseDTO, int amount) {}
    @Override public boolean validateBet(HorseDTO horse, int amount) { return true; }
    @Override public void getAllHorses() {}
    @Override public void createHorse(String name, int speedMin, int speedMax) {}
    @Override public void updateHorse(HorseDTO horse) {}
    @Override public void deleteHorse(HorseDTO horse) {}
    @Override public void update(String type, String payload) {}
    @Override public void setCurrentUser(UserDTO userDTO) {}
}