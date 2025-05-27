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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ViewModel for the GameView (race progress screen).
 *
 * Manages the horses, their positions, and status messages for a running race.
 * Binds to model properties and listens to race and horse position updates.
 */
public class GameViewVM implements ViewModel {

    private final ModelManager model;
    private final RaceDTO selectedRace;

    private final StringProperty statusText = new SimpleStringProperty("Race will start soon...");
    private final ObservableList<HorseDTO> horses = FXCollections.observableArrayList();
    private final Map<Integer, Integer> horsePositions = new HashMap<>();

    /**
     * Constructs a GameViewVM.
     *
     * @param model         the shared ModelManager that provides data and communication
     * @param selectedRace  the race this ViewModel is managing and visualizing
     */
    public GameViewVM(ModelManager model, RaceDTO selectedRace) {
        this.model = model;
        this.selectedRace = selectedRace;

        // Load horses from race data
        if (selectedRace != null && selectedRace.horses() != null) {
            horses.addAll(selectedRace.horses());

            for (HorseDTO horse : horses) {
                horsePositions.put(horse.id(), 0); // Start at position 0
            }
        }

        // Update horse positions based on model updates
        model.getHorsePositions().addListener((ListChangeListener<Integer>) this::handlePositionUpdate);

        // Update status text when the race starts
        model.raceStartedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                String raceName = model.currentRaceNameProperty().get();
                if (selectedRace.name().equals(raceName)) {
                    statusText.set("Race started: " + raceName);
                }
            }
        });
    }

    /**
     * @return Property containing race status text (e.g. countdown or "Race started").
     */
    public StringProperty statusTextProperty() {
        return statusText;
    }

    /**
     * @return List of horses participating in this race.
     */
    public List<HorseDTO> getHorses() {
        return horses;
    }

    /**
     * @return A map of horse IDs to their current positions on the track.
     */
    public Map<Integer, Integer> getHorsePositions() {
        return horsePositions;
    }

    /**
     * @return The length of the track for this race.
     * Returns 500 by default if track length is not defined.
     */
    public int getTrackLength() {
        if (selectedRace != null && selectedRace.raceTrack() != null) {
            return selectedRace.raceTrack().length();
        }
        return 500;
    }

    /**
     * Updates local horse position map with values from the model.
     * This method ensures all updates happen on the JavaFX thread.
     *
     * @param change the change event from the observed horse position list
     */
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
