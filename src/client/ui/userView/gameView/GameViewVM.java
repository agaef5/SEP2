package client.ui.userView.gameView;

import client.modelManager.ModelManager;
import client.ui.common.ViewModel;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import shared.DTO.HorseDTO;
import shared.DTO.RaceDTO;
import shared.updates.OnHorseFinished;
import shared.updates.OnRaceFinished;
import shared.updates.OnRaceStarted;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameViewVM implements ViewModel{

    private ModelManager model;
    private final RaceDTO selectedRace;

    private final StringProperty statusText = new SimpleStringProperty("Race will start soon...");
    private final ObservableList<HorseDTO> horses = FXCollections.observableArrayList();
    private final Map<Integer, Integer> horsePositions = new HashMap<>();

    public GameViewVM(ModelManager model, RaceDTO selectedRace) {
        this.model = model;
        this.selectedRace = selectedRace;

        // Initialize horses from race data
        if (selectedRace != null && selectedRace.horses() != null) {
            horses.addAll(selectedRace.horses());
            // Initialize all horses at position 0
            for (HorseDTO horse : horses) {
                horsePositions.put(horse.id(), 0);
            }
        }

        // Listen to horse positions updates
        model.getHorsePositions().addListener((ListChangeListener<Integer>) this::handlePositionUpdate);

        // Listen to race started property
        model.raceStartedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                String raceName = model.currentRaceNameProperty().get();
                if (selectedRace.name().equals(raceName)) {
                    statusText.set("Race started: " + raceName);
                }
            }
        });
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
        if (selectedRace != null && selectedRace.raceTrack() != null) {
            return selectedRace.raceTrack().length();
        }
        return 500; // Default length if not specified
    }

    private void handlePositionUpdate(ListChangeListener.Change<? extends Integer> change) {
        Platform.runLater(() -> {
            List<Integer> positions = model.getHorsePositions();
            for (int i = 0; i < positions.size() && i < horses.size(); i++) {
                HorseDTO horse = horses.get(i);
                horsePositions.put(horse.id(), positions.get(i));
            }
        });
    }


}