package client.ui.userView.landingPage;

import client.modelManager.ModelManager;
import client.ui.common.ViewModel;
import javafx.application.Platform;
import javafx.beans.property.*;
import shared.DTO.RaceDTO;
import shared.DTO.RaceState;

public class UserLandingPageVM implements ViewModel {

    private ModelManager model;

    //Properties
    private final StringProperty raceInfo = new SimpleStringProperty("No upcoming races");
    private final BooleanProperty bettingButtonDisabled = new SimpleBooleanProperty(true);

    private final BooleanProperty navigateToBetting = new SimpleBooleanProperty(false);
    private final IntegerProperty balanceInfo = new SimpleIntegerProperty(0);
    private RaceDTO selectedRace;

    public UserLandingPageVM(ModelManager model) {
        this.model = model;

        // Register as listener
        model.getNextRace().addListener((obs, oldRace, newRace) ->updateRaceInfo(newRace));

        //binding data to balance
        balanceInfo.bind(model.getUserBalance());

        // Initialize data
        model.getAllRaces();
    }

    public StringProperty raceInfoProperty() {
        return raceInfo;
    }
    public BooleanProperty bettingButtonDisabledProperty() {
        return bettingButtonDisabled;
    }
    public RaceDTO getSelectedRace() {
        return selectedRace;
    }
    public IntegerProperty balanceInfoProperty() {
        return balanceInfo;
    }


    // Get property indicating if navigation to betting stage is requested
    public BooleanProperty navigateToBettingProperty() {
        return navigateToBetting;
    }

    // Handle request to enter the betting stage
    public void enterBettingStage() {
        navigateToBetting.set(true);
    }

    // Reset navigation request after it has been handled
    public void resetNavigation() {
        navigateToBetting.set(false);
    }


    //Update race information based on the received race list
    private void updateRaceInfo(RaceDTO race) {
        Platform.runLater(() -> {
            if (race != null) {
                    selectedRace = race;

                // Update button disabled state based on race state
                bettingButtonDisabled.set(selectedRace.raceState() != RaceState.NOT_STARTED);

                // Display different message based on the race state
                if (selectedRace.raceState() == RaceState.NOT_STARTED) {
                    // Format the race information for upcoming race
                    String info = String.format("Next race: %s -%nTrack: %s -%nHorses: %d",
                            selectedRace.name(),
                            selectedRace.raceTrack().name(),
                            selectedRace.horses().size());

                    raceInfo.set(info);
                } else if (selectedRace.raceState() == RaceState.IN_PROGRESS) {
                    raceInfo.set("This race is in progress");
                } else if (selectedRace.raceState() == RaceState.FINISHED) {
                    raceInfo.set("The last race has finished. Waiting for the next race...");
                }
            } else {
                raceInfo.set("No upcoming races");
                selectedRace = null;
                bettingButtonDisabled.set(true); // Disable button when no races are available
            }
        });
    }
}