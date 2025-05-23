package client.ui.userView.landingPage;

import client.modelManager.ModelManager;
import client.ui.common.ViewModel;
import javafx.application.Platform;
import javafx.beans.property.*;
import shared.DTO.RaceDTO;
import shared.DTO.RaceState;

/**
 * ViewModel for the user landing page.
 * Handles the interaction logic and exposes properties to the controller for data binding.
 */
public class UserLandingPageVM implements ViewModel {

    //Properties
    private final StringProperty raceInfo = new SimpleStringProperty("No upcoming races");
    private final BooleanProperty bettingButtonDisabled = new SimpleBooleanProperty(true);
    private final BooleanProperty navigateToBetting = new SimpleBooleanProperty(false);
    private final IntegerProperty balanceInfo = new SimpleIntegerProperty(0);
    private ModelManager modelManager;
    private RaceDTO selectedRace;

    /**
     * Constructor for the UserLandingPageVM.
     * Initializes listeners and binds user balance.
     *
     * @param model the ModelManager handling data logic
     */
    public UserLandingPageVM(ModelManager model) {
        this.modelManager = model;

        // Register as listener
        model.getNextRace().addListener((obs, oldRace, newRace) ->updateRaceInfo(newRace));

        //binding data to balance
        balanceInfo.bind(model.getUserBalance());

        // Initialize data
        model.getAllRaces();
    }

//    Properties used and bound by Controller
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
        return modelManager.getUserBalance();
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


    /**
     * Updates the race information based on the provided race.
     * Updates the view properties on the JavaFX Application Thread.
     *
     * @param race the RaceDTO to update with
     */
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
                // Handle case when no race is available
                raceInfo.set("No upcoming races");
                selectedRace = null;
                bettingButtonDisabled.set(true); // Disable button when no races are available
            }
        });
    }
}