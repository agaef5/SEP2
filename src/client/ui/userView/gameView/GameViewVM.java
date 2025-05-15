package client.ui.userView.gameView;

import client.networking.SocketService;
import client.ui.common.MessageListener;
import client.ui.common.ViewModel;
import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shared.DTO.HorseDTO;
import shared.DTO.RaceDTO;
import shared.updates.OnHorseFinished;
import shared.updates.OnRaceFinished;
import shared.updates.OnRaceStarted;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameViewVM implements ViewModel, MessageListener {

    private final SocketService socketService;
    private final Gson gson;
    private final RaceDTO raceData;

    private final StringProperty statusText = new SimpleStringProperty("Race will start soon...");
    private final ObservableList<HorseDTO> horses = FXCollections.observableArrayList();
    private final Map<Integer, Integer> horsePositions = new HashMap<>();

    public GameViewVM(SocketService socketService, RaceDTO raceData) {
        this.socketService = socketService;
        this.gson = new Gson();
        this.raceData = raceData;

        // Initialize horses and positions
        if (raceData != null && raceData.horses() != null) {
            horses.addAll(raceData.horses());

            // Initialize all horses at position 0
            for (HorseDTO horse : horses) {
                horsePositions.put(horse.id(), 0);
            }
        }

        this.socketService.addListener(this);
    }

    public StringProperty statusTextProperty() {
        return statusText;
    }

    public List<HorseDTO> getHorses() {
        return horses;
    }

    public Map<Integer, Integer> getHorsePositions() {
        return horsePositions;
    }

    public int getTrackLength() {
        if (raceData != null && raceData.raceTrack() != null) {
            return raceData.raceTrack().length();
        }
        return 500; // Default length if not specified
    }

    public void handleRaceStarted(String payload) {
        OnRaceStarted raceStarted = gson.fromJson(payload, OnRaceStarted.class);
        Platform.runLater(() -> {
            statusText.set("Race started: " + raceStarted.raceName());
        });
    }

    public void handleHorseMoveUpdate(String payload) {
        // Parse the positions array from the payload
        int[] positions = gson.fromJson(payload, int[].class);

        Platform.runLater(() -> {
            // Update horse positions
            for (int i = 0; i < positions.length && i < horses.size(); i++) {
                HorseDTO horse = horses.get(i);
                horsePositions.put(horse.id(), positions[i]);
            }
        });
    }

    public void handleHorseFinished(String payload) {
        OnHorseFinished horseFinished = gson.fromJson(payload, OnHorseFinished.class);
        Platform.runLater(() -> {
            statusText.set(horseFinished.horseDTO().name() + " finished in position " + horseFinished.position());
        });
    }

    public void handleRaceFinished(String payload) {
        OnRaceFinished raceFinished = gson.fromJson(payload, OnRaceFinished.class);
        Platform.runLater(() -> {
            statusText.set("Race finished! Winner: " +
                    (raceFinished.finalPositionsDTO().isEmpty() ? "None" : raceFinished.finalPositionsDTO().get(0).name()));
        });
    }

    @Override
    public void update(String type, String payload) {
        switch (type) {
            case "onRaceStarted":
                handleRaceStarted(payload);
                break;
            case "horseMoveUpdate":
                handleHorseMoveUpdate(payload);
                break;
            case "onHorseFinished":
                handleHorseFinished(payload);
                break;
            case "onRaceFinished":
                handleRaceFinished(payload);
                break;
        }
    }

    public void cleanup() {
        socketService.removeListener(this);
    }
}